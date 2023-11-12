package com.pasha.telegramclone.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.databinding.FragmentChatsBinding
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.hideKeyboard


class MainFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Telegram"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }


}