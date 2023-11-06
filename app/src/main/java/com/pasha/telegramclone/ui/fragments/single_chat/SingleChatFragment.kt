package com.pasha.telegramclone.ui.fragments.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentSingleChatBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.models.UserModel
import com.pasha.telegramclone.ui.fragments.BaseFragment
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.NODE_MESSAGES
import com.pasha.telegramclone.database.NODE_USERS
import com.pasha.telegramclone.database.REF_DATABASE_ROOT
import com.pasha.telegramclone.database.TYPE_TEXT
import com.pasha.telegramclone.utilits.downloadAndSetImage
import com.pasha.telegramclone.database.getCommonModel
import com.pasha.telegramclone.database.getUserModel
import com.pasha.telegramclone.database.sendMessage
import com.pasha.telegramclone.utilits.AppChildEventListener
import com.pasha.telegramclone.utilits.showToast
import de.hdodenhof.circleimageview.CircleImageView

//contact - наш собеседник(все его данные)
class SingleChatFragment(private val contact: CommonModel) : BaseFragment() {
    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference

    //блок для отображения сообщений в чате
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener//слушатель изменений ребёнка основной ноды
    private var mCountMessages  = 10//хранит количество сообщений, которое нам нужно подгрузить при открытии чата
    private var mIsScrolling = false//состояние скроллинга
    private var mSmoothScrollToPosition = true//переменная, чтобы не перемещаться к последнему сообщению при скролинге чата вверх(обновлении данных)
    private var mListListeners = mutableListOf<AppChildEventListener>()//список для слушателей "детей"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerViev()
    }

    private fun initRecyclerViev() {
        mRecyclerView = binding.chatRecyclerView
        mAdapter = SingleChatAdapter()
        mRecyclerView.adapter = mAdapter
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(contact.id)

        //слушатель изменений в чате
        mMessagesListener = AppChildEventListener{
            mAdapter.addItem(it.getCommonModel())//передаём в адаптер одно по одному новому сообщению
            if (mSmoothScrollToPosition == true){
                mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)//листаем rcView к последнему элементу(чисто визуальноя часть, чтобы час не приходилось листать при новом сообщении)
            }

        }

        //подключили слушатель к  пути(показали что именно нужно слушать)//ограничиваем последними 10ю сообщениями
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListeners.add(mMessagesListener)//добавляем слушатель в список, для последущени удаления

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){//слушатель пролистывания rcView
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {//отслеживаем движение(скролинг)rcView
                super.onScrollStateChanged(recyclerView, newState)
                //если пользователь нажал на экран и скролит вверх или вниз
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {//отробатывает, когда произошло изменение rcView
                super.onScrolled(recyclerView, dx, dy)
                if(mIsScrolling && dy < 0){//если есть движения(НАВЕРХ), то обновляем даннные
                    updateData()
                }
            }
        })
    }

    private fun updateData() {//функция подгружает сообщения в чате, когда происходит скролл
        mSmoothScrollToPosition = false//перемещаться к последнему элементы не надо
        mIsScrolling = false//состояние скроллинга снова на false
        mCountMessages +=10//указываем, что нужно теперь подгрузить не 10, а 20+ элементов
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)//слушаем  БД, одгружаем последние 20 элементов из неё
        mListListeners.add(mMessagesListener)//добавляем слушатель в список, для последущени удаления
    }

    //при открытии чата менять инфу на тулбаре
    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbarInfo)
        mToolbarInfo.visibility = View.VISIBLE

        //слушатель изменений в БД
        mListenerInfoToolbar =
            AppValueEventListener {//it - пользователь из списка контактов на который мы нажали
                mReceivingUser = it.getUserModel()//записали данныен из БД в объект
                initInfoToolbar()
            }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS)
            .child(contact.id)//путь до данных пользователя, чат с которым открыли
        mRefUser.addValueEventListener(mListenerInfoToolbar)//подключили слушатель к данному пользователю

        //слушатель кнопки "оправки сообщения"
        binding.bChatSendMessage.setOnClickListener {
            mSmoothScrollToPosition = true//перемещаться к последнему сообщению
            val message = binding.chatInputMessage.text.toString()//получили введённое сообщение
            if (message.isEmpty()) {
                showToast(getString(R.string.chat_enter_a_message))
            } else sendMessage(message,
                contact.id,
                TYPE_TEXT)
            {
                binding.chatInputMessage.setText("")//чистим поле после отправки текста
            }

        }
    }

    //присвоили нашему toolbar(у) информацию о пользователе (Картинка, Имя, Статус)
    private fun initInfoToolbar() {
        if (mReceivingUser.fullname.isEmpty()) {//если пользователь не назвал себя
            mToolbarInfo.findViewById<TextView>(R.id.toolbarContactChatFullname).text =
                contact.fullname//имя из тел. книги
        } else mToolbarInfo.findViewById<TextView>(R.id.toolbarContactChatFullname).text =
            mReceivingUser.fullname//иначе имя из БД

        mToolbarInfo.findViewById<CircleImageView>(R.id.toolbarChatImage)
            .downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.findViewById<TextView>(R.id.toolbarContactsChatState).text =
            mReceivingUser.state
    }


    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE

        //при выходе из чата отключаем слушатель данных собеседника
        mRefUser.removeEventListener(mListenerInfoToolbar)

        mListListeners.forEach{AppChildListener->//бежим по списку слушателей и отключаем их
            mRefMessages.removeEventListener(AppChildListener)

        }


    }


}