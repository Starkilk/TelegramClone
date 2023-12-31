package com.pasha.telegramclone.ui.screens.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.NODE_MAIN_LIST
import com.pasha.telegramclone.database.NODE_MESSAGES
import com.pasha.telegramclone.database.NODE_USERS
import com.pasha.telegramclone.database.REF_DATABASE_ROOT
import com.pasha.telegramclone.database.getCommonModel
import com.pasha.telegramclone.databinding.AddContactsItemBinding
import com.pasha.telegramclone.databinding.FragmentAddContactsBinding

import com.pasha.telegramclone.databinding.FragmentMainListBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.ui.screens.base.BaseFragment
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.hideKeyboard
import com.pasha.telegramclone.utilits.replaceFragment


class AddContactsFragment : BaseFragment() {

    private lateinit var binding: FragmentAddContactsBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter:AddContactsAdapter

    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "New Group"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
        binding.bAddContactsNext.setOnClickListener {
            //переходим во фрагмент с дальнейшеё настройкой группы, передаёв на него выбранных участников
            replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = binding.addContactsRecyclerView
        mAdapter = AddContactsAdapter()

        ////1 ЗАПРОС(все пользователи с перепиской)
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener{ dataSnapshot ->
            //получаем id всех людей, с которыми есть переписка, записываем ID собесодников в модель и преобразуем список в мапу
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach {model ->

            ////2 ЗАПРОС(данные пользователей с перепиской)
                //бежим по всем объектам мапы, по ID обращаемся к ноде с данными о пользователе и строим новую модель, которую отпр. в адаптер
                mRefUsers.child(model.id).addListenerForSingleValueEvent(AppValueEventListener{ dataSnapshot1->
                    val newModel = dataSnapshot1.getCommonModel()

                    ////3 ЗАПРОС(последнее сообщение с каждым пользователем)
                    //обращаемся к ноде с сообщениями по ID и берём оттуда последний элемент(ТОЕСТЬ ПОСЛЕДНЕЕ СООБЩЕНИЕ)
                    mRefMessages.child(model.id).limitToFirst(1).addListenerForSingleValueEvent(AppValueEventListener{ dataSnapshot2 ->
                        val tempList = dataSnapshot2.children.map { it.getCommonModel() }//помещаем последнее сообщение в список

                        if(tempList.isEmpty()){
                            newModel.lastMessage = "Chat clear"
                        }else{
                            newModel.lastMessage = tempList[0].text//помещаем последнее сообщение в модель
                        }

                        if(newModel.fullname.isEmpty()){//если нет имени пользователя
                            newModel.fullname = newModel.phone
                        }
                        mAdapter.updateListItems(newModel)//передали модель в адаптер
                    })
                })
            }

        })

        mRecyclerView.adapter = mAdapter

    }

    companion object{
        val listContacts = mutableListOf<CommonModel>()
    }

}