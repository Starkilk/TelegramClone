package com.pasha.telegramclone.activities

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.pasha.telegramclone.databinding.ActivityMainBinding
import com.pasha.telegramclone.ui.fragments.MainFragment
import com.pasha.telegramclone.ui.fragments.register.EnterPhoneNumberFragment
import com.pasha.telegramclone.ui.objects.AppDrawer
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.database.AUTH
import com.pasha.telegramclone.utilits.AppStates
import com.pasha.telegramclone.utilits.READ_CONTACTS
import com.pasha.telegramclone.utilits.initContacts
import com.pasha.telegramclone.database.initFirebase
import com.pasha.telegramclone.database.initUser
import com.pasha.telegramclone.utilits.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer//передали в мэйн активити наш класс AppDrawer
    lateinit var mToolbarInfo: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APP_ACTIVITY = this
        initFirebase()//инициализация переменных

        //запускается инициализация пользователя и только после этого выполнятся последующие инициализации
        initUser{//инициализация пользователя

            //КАРУТИНА НЕ РОБИТ
            //пролизводим считываниек контактов в отдельной корутине
            //CoroutineScope(Dispatchers.IO).launch {
                initContacts()//разрешение на считывание контактов пользователя
            //}

            initFields()//инициализация полей
            initFunc()//инициализация функциональности активити
        }

    }

    private fun initFunc() {
        //проверка на авторизованность пользователя
        setSupportActionBar(mToolbar)//передаём НАШ тулбар на место тулбара по умолчанию
        if(AUTH.currentUser != null){
            //если АВТОРИЗОВАН
            mAppDrawer.create()//вызвали методы, которы находятся в нашем классе AppDrawer
            replaceFragment(MainFragment(),false)//при запускек активити - открыть фрагмент с чатами
        }else{
            //если НЕ АВТОРИЗОВАН
            replaceFragment(EnterPhoneNumberFragment(), false)
        }

    }

    private fun initFields() {
        mToolbar = binding.mainToolbar
        mToolbarInfo = binding.mainToolbar
        //проинициализировали наш класс с методами Drawer(а)
        mAppDrawer = AppDrawer()
        //инициализируем базу данных

    }

    //метод, который сворацивает клавиатуру после подтверждения введённых данных
    fun hideKeyboard(){
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken,0)
    }

    //получает результат разрешения и обробатывает разрешение или запрет пользователя
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //если доступ к контактам предоставлен
        if(ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }

    //отработает, когда разворачиваем приложение
    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    //отработает, когда сворачиваем приложение
    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }
}