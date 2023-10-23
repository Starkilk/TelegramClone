package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.R
import com.pasha.telegramclone.activities.MainActivity
import com.pasha.telegramclone.databinding.FragmentChangeNameBinding
import com.pasha.telegramclone.utilits.CHILD_FULLNAME
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.UID
import com.pasha.telegramclone.utilits.USER
import com.pasha.telegramclone.utilits.showToast


class ChangeNameFragment : Fragment() {
  private lateinit var binding: FragmentChangeNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentChangeNameBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        //при запуске фрагмента настроек Draewer отключается
        (activity as MainActivity).mAppDrawer.disableDrawer()
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)//включили наше меню c галочкой
        //достали имя и фамилию из USER и при открытии Edit Name данные уже будут в поляях
        val fullnameList = USER.fullname.split(" ")
        binding.settingsInputName.setText(fullnameList[0])
        binding.settingsInputSurname.setText(fullnameList[1])
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm,menu)//надулим разметку нашего меню

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_confirm_change -> changeName()
        }
        return true
    }

    //функция октивируемая при нажатии на галочку(сохранить изменённое имя)
    private fun changeName() {
        val name = binding.settingsInputName.text.toString()
        val sername = binding.settingsInputSurname.text.toString()

        if(name.isEmpty()){//если поле name пустое, то Toast
            showToast(getString(R.string.setting_toast_name_is_empty))
        }else{
            //иначе заменяем fullname в Firebase(на сайте)
            val fullname = "$name $sername"
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULLNAME)//добрались до пункта fullname в БД
                .setValue(fullname).addOnCompleteListener{//обновляем fullname
                    if(it.isSuccessful){
                        showToast(getString(R.string.toast_data_update))
                        USER.fullname = fullname//обновили fullname
                        parentFragmentManager.popBackStack()//вернулись назад по стэку
                    }
                }
        }
    }

    override fun onStop() {
        super.onStop()
        //при остановке фрагмента настроек Draewer включается
        (activity as MainActivity).mAppDrawer.enableDrawer()
    }
}