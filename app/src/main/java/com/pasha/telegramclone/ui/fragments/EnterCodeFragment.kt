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
        binding.registerInputCode.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val string = binding.registerInputCode.text.toString()
                if(string.length == 6){
                    verifiCode()
                }
            }

        })
    }

    private fun verifiCode() {
        Toast.makeText(activity,"Okey",Toast.LENGTH_SHORT).show()
    }
}