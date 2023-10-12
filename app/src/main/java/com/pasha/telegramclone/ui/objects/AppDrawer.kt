package com.pasha.telegramclone.ui.objects

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout

import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.pasha.telegramclone.R
import com.pasha.telegramclone.ui.fragments.SettingsFragment
import com.pasha.telegramclone.utilits.replaceFragment

class AppDrawer(val mainActivity:AppCompatActivity,  val toolbar: Toolbar) {
    private lateinit var mDrawer: Drawer//Drawer_layout выдвижное окно
    private lateinit var mHeader: AccountHeader//часть в Drawer где будет аватар, имя аккаунта и тд
    private lateinit var mDrawerLayout: DrawerLayout

    //метод, который мы вызовем на MainActivity после создания экземпляра этого класса
    fun create(){
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    //функция, которая будет откулючать выдвижное меню
    fun disableDrawer(){
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false//отключили гамбургер
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)//на место гамбургера поставили кнопку "Назад"
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)//заблокировали Drawer в закрытом состоянии
        toolbar.setNavigationOnClickListener {//что делаеть принажатии кнопки "Назад"(возвращаемся назад по стеку)
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    //функция, которая будет включать выдвижное меню
    fun enableDrawer(){
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)//отключили кнопку "Назад"
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true//включили гамбургер
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)//разблокировали Drawer\
        toolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()//открываем Drawer
        }
    }

    //функция отрисовки Drawer menu
    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(mainActivity)
            .withToolbar(toolbar)
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
                    //дейстрие при нажатии на пункт по позиции
                    when(position){
                        6 -> mainActivity.replaceFragment(SettingsFragment())
                    }
                    return false
                }

            }).build()


    }

    private fun createHeader() {//создали Header
        mHeader = AccountHeaderBuilder()
            .withActivity(mainActivity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Pavel")
                    .withEmail("+743244113")
            ).build()
    }
}