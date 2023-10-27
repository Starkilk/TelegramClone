package com.pasha.telegramclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.ActivityMainBinding
import com.pasha.telegramclone.models.User
import com.pasha.telegramclone.ui.fragments.ChatsFragment
import com.pasha.telegramclone.ui.objects.AppDrawer
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AUTH
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.CHILD_PHOTO_URL
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.CURRENT_UID
import com.pasha.telegramclone.utilits.FOLDER_PROFILE_IMAGE
import com.pasha.telegramclone.utilits.REF_STORAGE_ROOT
import com.pasha.telegramclone.utilits.USER
import com.pasha.telegramclone.utilits.initFirebase
import com.pasha.telegramclone.utilits.replaceActivity
import com.pasha.telegramclone.utilits.replaceFragment
import com.pasha.telegramclone.utilits.showToast
import com.theartofdev.edmodo.cropper.CropImage

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
        APP_ACTIVITY = this
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

    //считываем данные из бд в объект User
    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)//добрались до данных о польхователе
            .addListenerForSingleValueEvent(AppValueEventListener{//слушатель, который смотрит информацию из БД
                USER = it.getValue(User::class.java) ?:User()//вписали в нашего USER(a) данные из бд
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null){

            val uri = CropImage.getActivityResult(data).uri//получаем результат обрезания
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)//путь
                .child(CURRENT_UID)
            path.putFile(uri).addOnCompleteListener{task1->//передали в storage наше фото

                if (task1.isSuccessful){
                    path.downloadUrl.addOnCompleteListener {task2->

                        if(task2.isSuccessful){
                            val photoUrl = task2.result.toString()//получаем адрес в интернете, по которому мы обращаемся к картинке
                            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
                                .child(CHILD_PHOTO_URL).setValue(photoUrl)//в БД поместили url нашей фотографии
                                .addOnCompleteListener {
                                    if (it.isSuccessful){
                                        showToast(getString(R.string.toast_data_update))
                                        USER.photoUrl = photoUrl//в объект записали url нашей фотографии
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    //метод, который сворацивает клавиатуру после подтверждения введённых данных
    fun hideKeyboard(){
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken,0)
    }
}