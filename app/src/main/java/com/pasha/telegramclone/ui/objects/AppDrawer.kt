package com.pasha.telegramclone.ui.objects

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

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

class AppDrawer(val mainActivity:AppCompatActivity,  val toolbar: Toolbar) {
    private lateinit var mDrawer: Drawer//Drawer_layout выдвижное окно
    private lateinit var mHeader: AccountHeader//часть в Drawer где будет аватар, имя аккаунта и тд

    //метод, который мы вызовем на MainActivity после создания экземпляра этого класса
    fun create(){
        createHeader()
        createDrawer()
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
                        6 -> mainActivity.supportFragmentManager.beginTransaction()
                            .addToBackStack(null)//поместили фрагмент в Бэк Стэк
                            .replace(R.id.dataContainer, SettingsFragment()).commit()
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