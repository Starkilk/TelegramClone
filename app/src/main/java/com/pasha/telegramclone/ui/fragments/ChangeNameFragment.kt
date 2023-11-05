package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentChangeNameBinding
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.database.CHILD_FULLNAME
import com.pasha.telegramclone.database.NODE_USERS
import com.pasha.telegramclone.database.REF_DATABASE_ROOT
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.USER
import com.pasha.telegramclone.database.setNameToDatabase
import com.pasha.telegramclone.utilits.showToast


class ChangeNameFragment : BaseChangeFragment() {
  private lateinit var binding: FragmentChangeNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentChangeNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initFullname()
    }
    private fun initFullname() {
        //достали имя и фамилию из USER и при открытии Edit Name данные уже будут в полях
        val fullnameList = USER.fullname.split(" ")
        //проверка, чтобы приложение не крашилась при отсутствии ветки fullname в БД у пользователя
        if (fullnameList.size > 1){
            binding.settingsInputName.setText(fullnameList[0])
            binding.settingsInputSurname.setText(fullnameList[1])
        }else{
            binding.settingsInputName.setText(fullnameList[0])
        }
    }


    //функция октивируемая при нажатии на галочку(сохранить изменённое имя)
    override fun change() {
        val name = binding.settingsInputName.text.toString()
        val sername = binding.settingsInputSurname.text.toString()

        if(name.isEmpty()){//если поле name пустое, то Toast
            showToast(getString(R.string.setting_toast_name_is_empty))
        }else{
            //иначе заменяем fullname в Firebase(на сайте)
            val fullname = "$name $sername"
            setNameToDatabase(fullname)

        }
    }







}