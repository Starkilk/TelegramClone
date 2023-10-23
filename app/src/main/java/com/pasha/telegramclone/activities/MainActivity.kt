package com.pasha.telegramclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.ActivityMainBinding
import com.pasha.telegramclone.models.User
import com.pasha.telegramclone.ui.fragments.ChatsFragment
import com.pasha.telegramclone.ui.objects.AppDrawer
import com.pasha.telegramclone.utilits.AUTH
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.UID
import com.pasha.telegramclone.utilits.USER
import com.pasha.telegramclone.utilits.initFirebase
import com.pasha.telegramclone.utilits.replaceActivity
import com.pasha.telegramclone.utilits.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
     lateinit var mAppDrawer: AppDrawer//передали в мэйн активити наш класс AppDrawer




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

        //инициализируем базу данных
        initFirebase()
        initUser()
    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener{
                USER = it.getValue(User::class.java) ?:User()
            })
    }

}