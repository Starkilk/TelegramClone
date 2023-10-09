package com.pasha.telegramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.ActivityRegisterBinding
import com.pasha.telegramclone.ui.fragments.EnterPhoneNumberFragment
import com.pasha.telegramclone.utilits.replaceFragment

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //при старте данного активити открываем фрагмент: EnterPhoneNumberFragment
    override fun onStart() {
        super.onStart()
        mToolbar = binding.registerToolbar//инициализировали тулбар
        setSupportActionBar(mToolbar)//привязали тулбар
        title = getString(R.string.register_title_your_phone)//установили заголовок на тулбар

        //при старте данного активити открываем фрагмент: EnterPhoneNumberFragment
        replaceFragment(EnterPhoneNumberFragment())
    }
}