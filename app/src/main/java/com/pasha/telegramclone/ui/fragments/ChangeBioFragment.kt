package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentChangeBioBinding
import com.pasha.telegramclone.database.CHILD_BIO
import com.pasha.telegramclone.database.NODE_USERS
import com.pasha.telegramclone.database.REF_DATABASE_ROOT
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.USER
import com.pasha.telegramclone.database.setBioToDatabase
import com.pasha.telegramclone.utilits.showToast


class ChangeBioFragment : BaseChangeFragment() {
    private lateinit var binging:FragmentChangeBioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binging = FragmentChangeBioBinding.inflate(inflater,container,false)
        return binging.root
    }

    override fun onResume() {
        super.onResume()
        binging.settingsInputBio.setText(USER.bio)//установили в поле текст, который уже лежит в объекте пользователя
    }

    override fun change() {
        super.change()
        val newBio = binging.settingsInputBio.text.toString()
        setBioToDatabase(newBio)//вызов изменения Bio
    }


}