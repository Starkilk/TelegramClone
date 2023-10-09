package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentEnterCodeBinding
import com.pasha.telegramclone.databinding.FragmentEnterPhoneNumberBinding
import com.pasha.telegramclone.utilits.AppTextWatcher
import com.pasha.telegramclone.utilits.showToast


class EnterCodeFragment : Fragment() {
    private lateinit var binding: FragmentEnterCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterCodeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        //слушатель введённого текста в EditText c помощью выведеного нами в отдельный класс:TextWatcher(а)
        binding.registerInputCode.addTextChangedListener(AppTextWatcher{//передаём лямда функцию
                val string = binding.registerInputCode.text.toString()
                if(string.length == 6){
                    verifiCode()
                }
        })
    }

    private fun verifiCode() {
        showToast("Okey")
    }
}