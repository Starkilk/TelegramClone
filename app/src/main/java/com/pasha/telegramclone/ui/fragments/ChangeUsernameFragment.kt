package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentChangeUsernameBinding
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.database.CHILD_USERNAME
import com.pasha.telegramclone.database.NODE_USERNAMES
import com.pasha.telegramclone.database.NODE_USERS
import com.pasha.telegramclone.database.REF_DATABASE_ROOT
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.USER
import com.pasha.telegramclone.database.updateCurrentUsername
import com.pasha.telegramclone.utilits.showToast
import java.util.Locale


class ChangeUsernameFragment : BaseChangeFragment() {
    private lateinit var binding:FragmentChangeUsernameBinding

    lateinit var mNewUsername: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

    }
    override fun onResume() {
        super.onResume()
        //устанавливаем в поле для ввода нового идентификатора старое имя пользователя
        binding.settingsInputUsername.setText(USER.username)
    }



    override fun change() {//метод из BaseChangeFragment
        //поместили в переменную то, что пользователь ввёл
        mNewUsername = binding.settingsInputUsername.text.toString().lowercase(Locale.getDefault())
        if(mNewUsername.isEmpty()){
            showToast("the field is empty")
        }else{
            //проверка на уникальность идэнтификатора
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if(it.hasChild(mNewUsername)){
                        showToast("name is occupied")
                    }else{
                        changeUsername()
                    }
                })
        }
    }

    //создаём\добаляем новую ноду с идэнтификаторами пользователей и записываем в значение идэнтификатор
    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    updateCurrentUsername(mNewUsername)//обновление идэнтификатора пользователя в основной ветке
                }
            }
    }




    override fun onStop() {
        super.onStop()

    }
}