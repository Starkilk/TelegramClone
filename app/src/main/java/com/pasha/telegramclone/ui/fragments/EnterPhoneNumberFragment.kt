package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentEnterPhoneNumberBinding


class EnterPhoneNumberFragment : Fragment() {

private lateinit var binding: FragmentEnterPhoneNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterPhoneNumberBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        binding.bRegisterNext.setOnClickListener { sendCode() }
    }

    //функция проверки номера телефона
    private fun sendCode() {
        //если номер не введён:
        if(binding.registerInputPhoneNumber.text.toString().isEmpty()){
            Toast.makeText(activity, getString(R.string.register_toast_enter_phone),Toast.LENGTH_SHORT).show()
        }else{
            //если введён, то открываем: EnterCodeFragment
            fragmentManager?.beginTransaction()
                ?.replace(R.id.registerDataContainer,EnterCodeFragment())
                ?.addToBackStack(null)//записали в back stack
                ?.commit()
        }
    }


}