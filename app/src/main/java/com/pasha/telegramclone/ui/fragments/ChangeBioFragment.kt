package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentChangeBioBinding
import com.pasha.telegramclone.utilits.CHILD_BIO
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.CURRENT_UID
import com.pasha.telegramclone.utilits.USER
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
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(newBio)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    USER.bio = newBio
                    parentFragmentManager.popBackStack()
                }
            }
    }
}