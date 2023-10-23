package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.PhoneAuthProvider
import com.pasha.telegramclone.activities.MainActivity
import com.pasha.telegramclone.activities.RegisterActivity
import com.pasha.telegramclone.databinding.FragmentEnterCodeBinding
import com.pasha.telegramclone.utilits.AUTH
import com.pasha.telegramclone.utilits.AppTextWatcher
import com.pasha.telegramclone.utilits.CHILD_ID
import com.pasha.telegramclone.utilits.CHILD_PHONE
import com.pasha.telegramclone.utilits.CHILD_USERNAME
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.replaceActivity
import com.pasha.telegramclone.utilits.showToast


class EnterCodeFragment(val phoneNumber: String, val id: String) : Fragment() {
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
        (activity as RegisterActivity).title = phoneNumber
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
        val code =
            binding.registerInputCode.text.toString()//получаем код который юзер ввёл в EditText
        val credential = PhoneAuthProvider.getCredential(id, code)//передаём id и code

        //авторизация/регистрация пользователя
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {//если позьзователь авторизировался и всё хорошо
                val uid = AUTH.currentUser?.uid.toString()//проинициализировали id
                //вписываем все данные о пользователи в объект mutableMapOf, чтобы передать этот объект в БД
                //это всё выглядит наглядно в самом Firebase(сайт)
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber
                dateMap[CHILD_USERNAME] = uid

                //ЭТА СТРОЧКА ЛОМАЕТ
                //передали данные о пользователе в БД(создали ноду id пользователя и у него заполнили все данные)
                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                    .addOnCompleteListener { taskSecond ->
                        //действия после занесенияя данных в БД
                        if (taskSecond.isSuccessful) {//если task2.всё хорошо, то :
                            showToast("Welcome!")
                            (activity as RegisterActivity).replaceActivity(MainActivity())
                            //} else showToast("Mistake")//если не всё хорошо, то покажет проблему


                        } else showToast("Mistake")//если произошла ошибка


                    }
            }
        }
    }
}
