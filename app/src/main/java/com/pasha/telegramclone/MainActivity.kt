package com.pasha.telegramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
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


    //функция отрисовки Drawer menu
    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(mToolbar)
            .withActionBarDrawerToggle(true)//три полоски на toolbar чтобы вызвать выдвижное меню
            .withSelectedItem(-1)//какое меню будет открыто по умолчанию(Мы указываем, что никакое)
            .withAccountHeader(mHeader)
            .addDrawerItems(//собираем само выдвижное меню
                PrimaryDrawerItem()//СОЗДАТЬ ГРУППУ
                    .withIdentifier(100)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка вкл
                    .withName("New Group")//название пункта
                    .withSelectable(false)//выбран или нет
                    .withIcon(R.drawable.ic_menu_create_groups),

                PrimaryDrawerItem()//КОНТАКТЫ
                    .withIdentifier(101)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка
                    .withName("Contacts")//название пункта
                    .withSelectable(false)//выбран или нет
                    .withIcon(R.drawable.ic_menu_contacts),

                PrimaryDrawerItem()//ЗВОНКИ
                    .withIdentifier(102)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка
                    .withName("Calls")//название пункта
                    .withSelectable(false)//выбран или нет
                    .withIcon(R.drawable.ic_menu_phone),

                PrimaryDrawerItem()//СОЗДАТЬ КАНАЛ
                    .withIdentifier(103)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка
                    .withName("New channel")//название пункта
                    .withSelectable(false)//выбран или нет
                    .withIcon(R.drawable.ic_menu_create_channel),

                PrimaryDrawerItem()//ИЗБРАННОЕ
                    .withIdentifier(104)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка
                    .withName("Saved Messages")//название пункта
                    .withSelectable(false)//выбран или нет
                    .withIcon(R.drawable.ic_menu_favorites),

                PrimaryDrawerItem()//НАСТРОЙКИ
                    .withIdentifier(105)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка
                    .withName("Settings")//название пункта
                    .withSelectable(false)//выбран или нет
                    .withIcon(R.drawable.ic_menu_settings),

                DividerDrawerItem(),//разделитель(линия)

                PrimaryDrawerItem()//ПРИГЛАСИТЬ ДРУЗЕЙ
                    .withIdentifier(106)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка
                    .withName("Invite Friends")//название пункта
                    .withSelectable(false)//выбран или нет
                    .withIcon(R.drawable.ic_menu_invate),

                PrimaryDrawerItem()//ВОПРОСЫ О ТЕЛЕГРАМ
                    .withIdentifier(107)//идентификационный номер пункта в меню
                    .withIconTintingEnabled(true)//иконка
                    .withName("Telegram Features")//название пункта
                    .withSelectable(false)//выбран или нет
                    .withIcon(R.drawable.ic_menu_help),

                //слушатель нажатий на пункты меню
            ).withOnDrawerItemClickListener(object :Drawer.OnDrawerItemClickListener{
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    Toast.makeText(applicationContext, position.toString(),Toast.LENGTH_SHORT).show()
                    return false
                }

            }).build()


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