package com.pasha.telegramclone.ui.screens.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentEnterPhoneNumberBinding
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.database.AUTH
import com.pasha.telegramclone.utilits.replaceFragment
import com.pasha.telegramclone.utilits.restartActivity
import com.pasha.telegramclone.utilits.showToast
import java.util.concurrent.TimeUnit


class EnterPhoneNumberFragment : Fragment() {

    private lateinit var binding: FragmentEnterPhoneNumberBinding

    private lateinit var mPhoneNumber: String//номер телефона
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks//callback для PhoneAuthProvider


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //создаём объект mCallback
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            //объект (credential: PhoneAuthCredential) позволяет произвести авторизацию/создание пользователя
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {//если верефикация правильная
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {//если позьзователь авторизировался и всё хорошо
                        showToast("Welcome!")
                        restartActivity()//перезапуск активити
                    } else showToast(task.exception?.message.toString())//если произошла ошибка
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {//если верефикация НЕ правильная
                showToast(p0.message.toString())
            }

            //этот метод запускается тогда, когда отправлено СМС
            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                //СМС с кодом отправлено и нас перекидывает на фрагмент с заполнением поля для кода
                replaceFragment(EnterCodeFragment(mPhoneNumber,id))//передаём на фрагмент номер и id
            }
        }
        binding.bRegisterNext.setOnClickListener { sendCode() }

    }

    //функция проверки номера телефона
    private fun sendCode() {
        //если номер не введён:
        if (binding.registerInputPhoneNumber.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            //если введён, то открываем: EnterCodeFragment
            //replaceFragment(EnterCodeFragment())
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = binding.registerInputPhoneNumber.text.toString()
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions
                .newBuilder(FirebaseAuth.getInstance())
                .setActivity(APP_ACTIVITY)
                .setPhoneNumber(mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallback)
                .build()
        )
    }


}