package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.ContactItemBinding
import com.pasha.telegramclone.databinding.FragmentContactsBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.CURRENT_UID
import com.pasha.telegramclone.utilits.NODE_PHONES_CONTACTS
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.downloadAndSetImage
import com.pasha.telegramclone.utilits.getCommonModel
import de.hdodenhof.circleimageview.CircleImageView




class ContactsFragment : BaseFragment() {

private lateinit var binding:FragmentContactsBinding
private lateinit var mRecyclerView: RecyclerView
private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>//Firebase адаптер
private lateinit var mRefContacts:DatabaseReference//откуда скачивает данные(путь к нужной ноде и тд)
private lateinit var mRefUsers:DatabaseReference//путь к юзерам, чтобы с найти пользователей из тел. книги и заполнить их данными разметку contact_item
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Contacts"//заголовок
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = binding.contactsRecyclerView//ининициализация рецайклера
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)//запрос на считываение из Firebase(путь, куда сохраняем)
            .build()
        mAdapter = object :FirebaseRecyclerAdapter<CommonModel,ContactsHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)//надули разметку
                return ContactsHolder(view)//передали разметку в Holder, там эта разметка заполнится инициализируется
            }

            override fun onBindViewHolder(//заполняем разметку данными
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)//переходим в пользователя из телефонной книги
                mRefUsers.addValueEventListener(AppValueEventListener{

                    val contact = it.getCommonModel()//получили данные контакта, записав их в модель

                    //заполнем item контакта
                    holder.name.text = contact.fullname
                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                })


            }
        }
        mRecyclerView.adapter = mAdapter//присвоили адаптер
        mAdapter.startListening()//подключили слушатель к адаптеру
    }

    class ContactsHolder(view: View):RecyclerView.ViewHolder(view){
        private val itemBinding  = ContactItemBinding.bind(view)

        //захватываем элменты нашего contsct_item
        val name: TextView = itemBinding.contactFullname
        val status: TextView = itemBinding.contactsState
        val photo: CircleImageView = itemBinding.contactPhoto
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
    }
}

