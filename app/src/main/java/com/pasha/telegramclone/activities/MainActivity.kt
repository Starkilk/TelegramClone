package com.pasha.telegramclone.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.ActivityMainBinding
import com.pasha.telegramclone.models.User
import com.pasha.telegramclone.ui.fragments.ChatsFragment
import com.pasha.telegramclone.ui.objects.AppDrawer
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AUTH
import com.pasha.telegramclone.utilits.AppStates
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.CHILD_PHOTO_URL
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.CURRENT_UID
import com.pasha.telegramclone.utilits.FOLDER_PROFILE_IMAGE
import com.pasha.telegramclone.utilits.READ_CONTACTS
import com.pasha.telegramclone.utilits.REF_STORAGE_ROOT
import com.pasha.telegramclone.utilits.USER
import com.pasha.telegramclone.utilits.checkPermissions
import com.pasha.telegramclone.utilits.initContacts
import com.pasha.telegramclone.utilits.initFirebase
import com.pasha.telegramclone.utilits.initUser
import com.pasha.telegramclone.utilits.replaceActivity
import com.pasha.telegramclone.utilits.replaceFragment
import com.pasha.telegramclone.utilits.showToast
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
     lateinit var mAppDrawer: AppDrawer//передали в мэйн активити наш класс AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APP_ACTIVITY = this
        initFirebase()//инициализация переменных

        //запускается инициализация пользователя и только после этого выполнятся последующие инициализации
        initUser{//инициализация пользователя

            //пролизводим считываниек контактов в отдельной корутине
            //CoroutineScope(Dispatchers.IO).launch {
                initContacts()//разрешение на считывание контактов пользователя
            //}

            initFields()//инициализация полей
            initFunc()//инициализация функциональности активити
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
}