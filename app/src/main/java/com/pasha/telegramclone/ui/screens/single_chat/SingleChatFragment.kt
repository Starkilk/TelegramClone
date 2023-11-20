package com.pasha.telegramclone.ui.screens.single_chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.pasha.telegramclone.R
import com.pasha.telegramclone.database.AUTH
import com.pasha.telegramclone.databinding.FragmentSingleChatBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.models.UserModel
import com.pasha.telegramclone.ui.screens.BaseFragment
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.NODE_MESSAGES
import com.pasha.telegramclone.database.NODE_USERS
import com.pasha.telegramclone.database.REF_DATABASE_ROOT
import com.pasha.telegramclone.database.TYPE_TEXT
import com.pasha.telegramclone.database.clearChat
import com.pasha.telegramclone.database.deleteChat
import com.pasha.telegramclone.utilits.downloadAndSetImage
import com.pasha.telegramclone.database.getCommonModel
import com.pasha.telegramclone.database.getMessageKey
import com.pasha.telegramclone.database.getUserModel
import com.pasha.telegramclone.database.saveToMainList
import com.pasha.telegramclone.database.sendMessage
import com.pasha.telegramclone.database.uploadFileToStorage
import com.pasha.telegramclone.ui.screens.main_list.MainListFragment
import com.pasha.telegramclone.ui.screens.message_recycler_view.views.AppViewFactory
import com.pasha.telegramclone.ui.screens.settings.ChangeNameFragment
import com.pasha.telegramclone.utilits.AppChildEventListener
import com.pasha.telegramclone.utilits.AppStates
import com.pasha.telegramclone.utilits.AppTextWatcher
import com.pasha.telegramclone.utilits.AppVoiceRecorder
import com.pasha.telegramclone.utilits.PICK_FILE_REQUEST_CODE
import com.pasha.telegramclone.utilits.RECORD_AUDIO
import com.pasha.telegramclone.utilits.TYPE_CHAT
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_FILE
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_IMAGE
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_VOICE
import com.pasha.telegramclone.utilits.checkPermissions
import com.pasha.telegramclone.utilits.getFilenameFromUri
import com.pasha.telegramclone.utilits.replaceFragment
import com.pasha.telegramclone.utilits.restartActivity
import com.pasha.telegramclone.utilits.showToast
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private var mCountMessages  = 15//хранит количество сообщений, которое нам нужно подгрузить при открытии чата
    private var mIsScrolling = false//состояние скроллинга
    private var mSmoothScrollToPosition = true//переменная, чтобы не перемещаться к последнему сообщению при скролинге чата вверх(обновлении данных)
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager//тип отображения сообщений
    private lateinit var mAppVoiceRecorder:AppVoiceRecorder//экземпляр войс рекодера
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        initFields()
        initToolbar()
        initRecyclerViev()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        setHasOptionsMenu(true)//разрешение МЕНЮ
        //экземпляр нашего выдвижного меню с выбором того, что оправить
        mBottomSheetBehavior = BottomSheetBehavior.from(binding.coordinator.findViewById<LinearLayout>(R.id.bottomSheetChoice))
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN//изначально меню скрыто
        mAppVoiceRecorder = AppVoiceRecorder()
        mSwipeRefreshLayout = binding.chatSwipeRefresh
        mLayoutManager = LinearLayoutManager(this.context)
        //отправка картинки\отправка сообщения
        binding.chatInputMessage.addTextChangedListener (AppTextWatcher{
            val string = binding.chatInputMessage.text.toString()
            if(string.isEmpty() || string == "Recording"){//показываем "скрепка" и "микрофон"
                binding.bChatSendMessage.visibility = View.GONE
                binding.bChatAttach.visibility = View.VISIBLE
                binding.bChatVoice.visibility = View.VISIBLE
            }else{//показываем "отправить"
                binding.bChatSendMessage.visibility = View.VISIBLE
                binding.bChatAttach.visibility = View.GONE
                binding.bChatVoice.visibility = View.GONE

            }
        })

        //слушатель на скрепку(запускает функцию отправки файлов)
        binding.bChatAttach.setOnClickListener { attach() }

        //отдельная корутина(запись\ остановка записи ГС)
        CoroutineScope(Dispatchers.IO).launch {
            //слушатель удерживания кнопки "голосового сообщения"(View, event)
            binding.bChatVoice.setOnTouchListener { v, event ->
                if(checkPermissions(RECORD_AUDIO)){//если есть разрешение на запись голоса
                    if (event.action == MotionEvent.ACTION_DOWN){//если кнопка нажата
                        //TODO record
                        binding.chatInputMessage.setText("Recording")
                        binding.bChatVoice.setColorFilter(ContextCompat.getColor(APP_ACTIVITY, com.mikepenz.materialize.R.color.primary))//меняем цвет
                        val messageKey = getMessageKey(contact.id)//получаем уникальный ключ сообщения
                        mAppVoiceRecorder.startRecord(messageKey)//начали запись
                    }else if(event.action == MotionEvent.ACTION_UP){//если кнопку отжали
                        //TODO stop record
                        binding.chatInputMessage.setText("")
                        binding.bChatVoice.colorFilter = null//сбрасываем цвет
                        mAppVoiceRecorder.stopRecord{file, messageKey->//остановили запись
                            uploadFileToStorage(Uri.fromFile(file),messageKey,contact.id, TYPE_MESSAGE_VOICE)//загружаем файл в хранилище
                            mSmoothScrollToPosition = true

                        }
                    }
                }
                true
            }
        }
    }

    private fun attach() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED//открыли меню
        //получили 2 картинки из выдвижного меню
        val btnAttachFile = binding.coordinator.findViewById<ImageView>(R.id.bAttachFile)
        val btnAttachImage = binding.coordinator.findViewById<ImageView>(R.id.bAttachImage)
        //вешаем на них слушатели
        btnAttachFile.setOnClickListener { attachFile() }
        btnAttachImage.setOnClickListener { attachImage() }
    }

    //отправка файла
    private fun attachFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)//создали намерение
        intent.type = "*/*"//любое название/любое расширение
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    //отправка картинки
    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1,1)//указали, что кропер будет пропорционален
            .setRequestedSize(250,250)//обрезаем картинку, чтобы она занимала меньше места
            .start(APP_ACTIVITY, this)
    }

    private fun initRecyclerViev() {
        mRecyclerView = binding.chatRecyclerView
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(contact.id)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)//оптимизация-указывает, что все view у нас будут одинакового размера
        mRecyclerView.isNestedScrollingEnabled = false//оптимизация
        mRecyclerView.layoutManager = mLayoutManager

        //слушатель изменений в чате
        mMessagesListener = AppChildEventListener{
            val message = it.getCommonModel()//получили сообщение из объекта

            if (mSmoothScrollToPosition){//если нужно добавить сообщение вниз
                mAdapter.addItemToBottom(AppViewFactory.getView(message)){//отправляем (объект)сообщение в адаптер
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)//листаем rcView к последнему элементу(чисто визуальноя часть, чтобы час не приходилось листать при новом сообщении)
                }
            }else{//если нужно дорисовать старые сообщения сверху
                mAdapter.addItemToTop(AppViewFactory.getView(message)){//отправляем (объект)сообщение в адаптер
                    mSwipeRefreshLayout.isRefreshing = false//отклёчение значка загрузки после использования "резинки от трусов"
                }
            }
        }

        //подключили слушатель к  пути(показали что именно нужно слушать)//ограничиваем последними 10ю сообщениями
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

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
                //если есть движения(НАВЕРХ), то обновляем даннные && если это скролл длинной в 3 itemHoler(a)(это чтобы rc не дорисовывал +10 элементов при каждом, незначительном скролле вверх)
                if(mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3){
                    updateData()
                }
            }
        })

        //при натягивании "резинки от трусов" подгружать старые сообщения
        mSwipeRefreshLayout.setOnRefreshListener { updateData() }

    }

    private fun updateData() {//функция подгружает сообщения в чате, когда происходит скролл
        mSmoothScrollToPosition = false//перемещаться к последнему элементы не надо
        mIsScrolling = false//состояние скроллинга снова на false
        mCountMessages +=10//указываем, что нужно теперь подгрузить не 10, а 20+ элементов
        mRefMessages.removeEventListener(mMessagesListener)//удалили старого слушателя
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)//подклющаем нового, подгружаем последние 20 элементов из неё

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
                saveToMainList(contact.id, TYPE_CHAT,)

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null){
            when(requestCode){
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE->{
                    val uri = CropImage.getActivityResult(data).uri//получаем результат обрезания
                    val messageKey = getMessageKey(contact.id)//получаем уникальный ключ сообщения
                    uploadFileToStorage(uri,messageKey,contact.id, TYPE_MESSAGE_IMAGE)
                    mSmoothScrollToPosition = true
                }
                PICK_FILE_REQUEST_CODE ->{
                    val uri = data.data
                    val messageKey = getMessageKey(contact.id)//получаем уникальный ключ сообщения
                    val filename = getFilenameFromUri(uri!!)//получаем название файла по uri
                    uploadFileToStorage(uri,messageKey,contact.id, TYPE_MESSAGE_FILE,filename) //скачали файл в БД с хранилища
                    mSmoothScrollToPosition = true
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.single_chat_action_menu,menu)//определили меню на этом фрагменте
    }

    //функция для действий при нажатии на пункты иеню на тулбаре настроек
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){//слошаем что нажал юзер
            R.id.menuClearChat -> clearChat(contact.id){//нажал очистить чат
                showToast("Chat clear")
                replaceFragment(MainListFragment())
            }
            R.id.menuDeleteChat -> deleteChat(contact.id){//нажал удалить чат
                showToast("Chat deleted")
                replaceFragment(MainListFragment())
            }
        }
        return true
    }




    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE

        //при выходе из чата отключаем слушатель данных собеседника
        mRefUser.removeEventListener(mListenerInfoToolbar)
        //удалили слушателя
        mRefMessages.removeEventListener(mMessagesListener)
    }

    override fun onDestroy() {//при выходе из чата
        super.onDestroy()
        mAppVoiceRecorder.releaseRecorder()//удалили рекодер, когда вышли из чата
        mAdapter.onDestroy()//удаляем слушатели нажатий
    }

}