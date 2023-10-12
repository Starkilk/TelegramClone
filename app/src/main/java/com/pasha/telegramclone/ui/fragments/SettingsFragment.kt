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
import com.pasha.telegramclone.activities.RegisterActivity
import com.pasha.telegramclone.databinding.FragmentSettingsBinding
import com.pasha.telegramclone.utilits.AUTH
import com.pasha.telegramclone.utilits.replaceActivity
import com.pasha.telegramclone.utilits.replaceFragment


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onStart() {
        super.onStart()
        //при запуске фрагмента настроек Draewer отключается
        (activity as MainActivity).mAppDrawer.disableDrawer()
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)//включили наше меню в контексте

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu,menu)//определили меню на этом фрагменте
    }

    //функция для действий при нажатии на пункты иеню на тулбаре настроек
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){//слошаем что нажал юзер
            R.id.settingsMenuExit->{//если нажал выйти
                AUTH.signOut()//выходим из аккаунта
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
            //при виборе "Edit name" открываем ChangeNameFragment()
            R.id.settingsMenuChangeName -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        //при остановке фрагмента настроек Draewer включается
        (activity as MainActivity).mAppDrawer.enableDrawer()
    }
}