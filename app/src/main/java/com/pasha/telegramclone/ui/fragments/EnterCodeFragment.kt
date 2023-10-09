package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pasha.telegramclone.activities.MainActivity
import com.pasha.telegramclone.activities.RegisterActivity
import com.pasha.telegramclone.databinding.FragmentEnterCodeBinding
import com.pasha.telegramclone.utilits.AUTH
import com.pasha.telegramclone.utilits.AppTextWatcher
import com.pasha.telegramclone.utilits.replaceActivity
import com.pasha.telegramclone.utilits.showToast


class EnterCodeFragment(val mPhoneNumber: String, val id: String) : Fragment() {
    private lateinit var binding: FragmentEnterCodeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = mPhoneNumber
        //слушатель введённого текста в EditText c помощью выведеного нами в отдельный класс:TextWatcher(а)
        binding.registerInputCode.addTextChangedListener(AppTextWatcher {//передаём лямда функцию
            val string = binding.registerInputCode.text.toString()
            //вводится 6 цифр и сразу запускается enterCode()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code =  binding.registerInputCode.text.toString()//получаем код который юзер ввёл в EditText
        val credential = PhoneAuthProvider.getCredential(id,code)//передаём id и code

            //авторизация/регистрация пользователя
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {//если позьзователь авторизировался и всё хорошо
                showToast("Welcome!")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            } else showToast(task.exception?.message.toString())//если произошла ошибка
        }
    }
}