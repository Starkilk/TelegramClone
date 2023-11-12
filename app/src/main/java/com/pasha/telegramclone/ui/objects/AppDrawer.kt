package com.pasha.telegramclone.ui.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout

import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.pasha.telegramclone.R
import com.pasha.telegramclone.ui.screens.ContactsFragment
import com.pasha.telegramclone.ui.screens.SettingsFragment
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.database.USER
import com.pasha.telegramclone.utilits.downloadAndSetImage
import com.pasha.telegramclone.utilits.replaceFragment

class AppDrawer() {
    private lateinit var mDrawer: Drawer//Drawer_layout выдвижное окно
    private lateinit var mHeader: AccountHeader//часть в Drawer где будет аватар, имя аккаунта и тд
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mCurrentProfile: ProfileDrawerItem//итем нашего профиля(что уникального в нашем профиле, типо картинка, имя и тд)

    //метод, который мы вызовем на MainActivity после создания экземпляра этого класса
    fun create(){
        initLoader()
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    //функция, которая будет откулючать выдвижное меню
    fun disableDrawer(){
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false//отключили гамбургер
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)//на место гамбургера поставили кнопку "Назад"
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)//заблокировали Drawer в закрытом состоянии
        APP_ACTIVITY.mToolbar.setNavigationOnClickListener {//что делаеть принажатии кнопки "Назад"(возвращаемся назад по стеку)
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }

    //функция, которая будет включать выдвижное меню
    fun enableDrawer(){
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)//отключили кнопку "Назад"
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true//включили гамбургер
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)//разблокировали Drawer\
        APP_ACTIVITY.mToolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()//открываем Drawer
        }
    }

    //функция отрисовки Drawer menu
    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(APP_ACTIVITY)
            .withToolbar( APP_ACTIVITY.mToolbar)
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
                    clickToItem(position)
                    return false
                }

            }).build()
    }
    private fun clickToItem(position:Int){
        //дейстрие при нажатии на пункт по позиции
        when(position){
            6 -> replaceFragment(SettingsFragment())
            2 -> replaceFragment(ContactsFragment())
        }
    }

    private fun createHeader() {//создали Header
        mCurrentProfile = ProfileDrawerItem()//заполнили данные профиля
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)

        mHeader = AccountHeaderBuilder()
            .withActivity(APP_ACTIVITY)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
               mCurrentProfile//данные поместили во view драйвер меню
            ).build()
    }

    //метод обновляет данные в Header
    fun updateHeader(){
        mCurrentProfile//заполнили данные профиля
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)

        mHeader.updateProfile(mCurrentProfile)
    }

    private fun initLoader(){
        DrawerImageLoader.init(object :AbstractDrawerImageLoader(){
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {//метод который вставит во view картинку скаченую с интернета
                imageView.downloadAndSetImage(uri.toString())
            }
        })

    }
}