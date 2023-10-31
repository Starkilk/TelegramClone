package com.pasha.telegramclone.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.ActivityMainBinding
import com.pasha.telegramclone.databinding.FragmentSingleChatBinding
import com.pasha.telegramclone.databinding.ToolbarInfoBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.utilits.APP_ACTIVITY


class SingleChatFragment(contact: CommonModel) : BaseFragment() {
    private lateinit var binding: FragmentSingleChatBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleChatBinding.inflate(inflater,container, false)

        return binding.root
    }

    //РАЗОБРАТЬСЯ
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbarInfo).visibility = View.VISIBLE
    }


    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbarInfo).visibility = View.GONE



    }


}