package com.pasha.telegramclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.ActivityMainBinding
import com.pasha.telegramclone.ui.fragments.ChatsFragment
import com.pasha.telegramclone.ui.objects.AppDrawer
import com.pasha.telegramclone.utilits.AUTH
import com.pasha.telegramclone.utilits.replaceActivity
import com.pasha.telegramclone.utilits.replaceFragment

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
        //проверка на авторизованность пользователя
        if(AUTH.currentUser != null){
            //если АВТОРИЗОВАН
            setSupportActionBar(mToolbar)//передаём НАШ тулбар на место тулбара по умолчанию
            mAppDrawer.create()//вызвали методы, которы находятся в нашем классе AppDrawer
            replaceFragment(ChatsFragment(),false)//при запускек активити - открыть фрагмент с чатами
        }else{
            //если НЕ АВТОРИЗОВАН
            replaceActivity(RegisterActivity())
        }



    }

    private fun initFields() {
        mToolbar = binding.mainToolbar
        //проинициализировали наш класс с методами Drawer(а) передали туда наше активити и наш Toolbar
        mAppDrawer = AppDrawer(this,mToolbar)
        //проиниалицировали объект FirebaseAuth
        AUTH = FirebaseAuth.getInstance()
    }

}