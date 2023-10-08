package com.pasha.telegramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}