package com.pasha.telegramclone.ui.screens.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.R
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.NODE_MAIN_LIST
import com.pasha.telegramclone.database.NODE_MESSAGES
import com.pasha.telegramclone.database.NODE_USERS
import com.pasha.telegramclone.database.REF_DATABASE_ROOT
import com.pasha.telegramclone.database.getCommonModel
import com.pasha.telegramclone.databinding.FragmentAddContactsBinding
import com.pasha.telegramclone.databinding.FragmentCreateGroupBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.ui.screens.base.BaseFragment
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.hideKeyboard
import com.pasha.telegramclone.utilits.replaceFragment
import com.pasha.telegramclone.utilits.showToast


class CreateGroupFragment(private var listContacts:List<CommonModel>): BaseFragment() {
    private lateinit var binding:FragmentCreateGroupBinding

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter:AddContactsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateGroupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
        binding.bCreateGroupComplete.setOnClickListener { showToast("Click") }
        binding.createGroupInput.requestFocus()//при открытии фрагмента курсор сразу на вводе названия группы
    }

    private fun initRecyclerView() {
        mRecyclerView = binding.createGroupRecyclerView
        mAdapter = AddContactsAdapter()

        mRecyclerView.adapter = mAdapter
        //передаём элементы списка в адаптер
        listContacts.forEach { mAdapter.updateListItems(it) }
    }





}