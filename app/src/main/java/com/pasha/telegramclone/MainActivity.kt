package com.pasha.telegramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.pasha.telegramclone.databinding.ActivityMainBinding
import com.pasha.telegramclone.ui.ChatsFragment
import com.pasha.telegramclone.ui.objects.AppDrawer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar

    private lateinit var mAppDrawer: AppDrawer//передали в мэйн активити наш класс AppDrawer

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
        mAppDrawer.create()//вызвали методы, которы находятся в нашем классе AppDrawer
        //при запускек активити - открыть фрагмент с чатами
        supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer,ChatsFragment()).commit()

    }

    private fun initFields() {
        mToolbar = binding.mainToolbar
        //проинициализировали наш класс с методами Drawer(а) передали туда наше активити и наш Toolbar
        mAppDrawer = AppDrawer(this,mToolbar)
    }

}