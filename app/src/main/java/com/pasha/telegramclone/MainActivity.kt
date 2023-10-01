package com.pasha.telegramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.pasha.telegramclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDrawer: Drawer//Drawer_layout выдвижное окно
    private lateinit var mHeader: AccountHeader//часть в Drawer где будет аватар, имя аккаунта и тд
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()//иницифлизировать поля
        initFunc()//инициализация функциональности активити
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)//передаём НАШ тулбар на место тулбара по умолчанию
        createHeader()
        createDrawer()
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(mToolbar)
            .withActionBarDrawerToggle(true)//три полоски на toolbar чтобы вызвать выдвижное меню
            .withSelectedItem(-1)//какое меню будет открыто по умолчанию(Мы указываем, что никакое)
            .withAccountHeader(mHeader)
            .addDrawerItems(//собираем само выдвижное меню
                PrimaryDrawerItem()
                    .withIdentifier(100)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка
                    .withName("New Group")//название пункта
                    .withSelectable(false)//выбран или нет
            ).build()
    }

    private fun createHeader() {//создали Header
        mHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Pavel")
                    .withEmail("+743244113")
            ).build()
    }

    private fun initFields() {
        mToolbar = binding.mainToolbar
    }

}