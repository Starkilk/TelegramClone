package com.pasha.telegramclone.ui.screens

import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.pasha.telegramclone.R
import com.pasha.telegramclone.activities.MainActivity
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.hideKeyboard

//фрагмет, от которого будут наследоваться все фрагменты в которых происходит изменение информации о пользователе
open class BaseChangeFragment : Fragment() {//open, чтобы можно было наследоваться от него о overid(ить) методы

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)//включили наше меню c галочкой
        //при запуске фрагмента настроек Draewer отключается
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        //при остановке фрагмента настроек Draewer включается
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        APP_ACTIVITY.hideKeyboard()
    }

    //галочка
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm,menu)//надулим разметку нашего меню

    }
    //слушатель нажатия на галочку
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    open fun change() {

    }

}