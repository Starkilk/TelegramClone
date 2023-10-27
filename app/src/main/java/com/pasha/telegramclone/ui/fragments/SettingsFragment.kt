package com.pasha.telegramclone.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.firebase.storage.StorageReference
import com.pasha.telegramclone.R
import com.pasha.telegramclone.activities.MainActivity
import com.pasha.telegramclone.activities.RegisterActivity
import com.pasha.telegramclone.databinding.FragmentSettingsBinding
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AUTH
import com.pasha.telegramclone.utilits.CHILD_PHOTO_URL
import com.pasha.telegramclone.utilits.FOLDER_PROFILE_IMAGE
import com.pasha.telegramclone.utilits.REF_STORAGE_ROOT
import com.pasha.telegramclone.utilits.CURRENT_UID
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.USER
import com.pasha.telegramclone.utilits.donwloadAndSetImage
import com.pasha.telegramclone.utilits.getUrlFromStorage
import com.pasha.telegramclone.utilits.putImageToStorage
import com.pasha.telegramclone.utilits.putUrlToDatabase
import com.pasha.telegramclone.utilits.replaceActivity
import com.pasha.telegramclone.utilits.replaceFragment
import com.pasha.telegramclone.utilits.showToast
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class SettingsFragment : Fragment() {

private lateinit var binding:FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        //при запуске фрагмента настроек Draewer отключается
        (activity as MainActivity).mAppDrawer.disableDrawer()
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)//включили наше меню в контексте

        initFields()//инициализация текстовых блоков в окне настроек
    }

    //инициализация всех текстView в окне настроек(взятые из БД , помещённые в объект User данные, добавили во вьюшки)
    private fun initFields() {
        binding.settingsBio.text = USER.bio
        binding.settingsFullName.text = USER.fullname
        binding.settingsPhoneNumber.text = USER.phone
        binding.settingStatus.text = USER.status
        binding.settingsUsername.text = USER.username

        //переход на фрагмент изменения уникального имени пользователя
        binding.bSettingsChangeUsername.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }

        //переход на фрагмент изменения информации о пользователе
        binding.bSettingsChangeBio.setOnClickListener { replaceFragment(ChangeBioFragment()) }

        //слушатель нажатия на кнопу изменения фотографии
        binding.settingsChangePhoto.setOnClickListener { changePhotoUser() }

        //установка картинки при открытии меню настроек
        binding.settingsUserPhoto.donwloadAndSetImage(USER.photoUrl)
    }

    //метод изменения фото
    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1,1)//указали, что кропер будет пропорционален
            .setRequestedSize(600,600)//обрезаем картинку, чтобы она занимала меньше места
            .setCropShape(CropImageView.CropShape.OVAL)//делаем картинку овальной
            .start(APP_ACTIVITY, this)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu,menu)//определили меню на этом фрагменте
    }

    //функция для действий при нажатии на пункты иеню на тулбаре настроек
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){//слошаем что нажал юзер
            R.id.settingsMenuExit->{//если нажал выйти
                AUTH.signOut()//выходим из аккаунта
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
            //при виборе "Edit name" открываем ChangeNameFragment()
            R.id.settingsMenuChangeName -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        //при остановке фрагмента настроек Draewer включается
        (activity as MainActivity).mAppDrawer.enableDrawer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null){

            val uri = CropImage.getActivityResult(data).uri//получаем результат обрезания
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)//путь
                .child(CURRENT_UID)
            
            putImageToStorage(uri,path){
                //этот код запистится после отработки слушателя
                getUrlFromStorage(path){ourUrl ->
                    putUrlToDatabase(ourUrl){
                        binding.settingsUserPhoto.donwloadAndSetImage(ourUrl)//установка картинки
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = ourUrl//в объект записали url нашей фотографии
                    }
                }
            }


        }
    }


}