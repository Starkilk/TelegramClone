package com.pasha.telegramclone.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentSettingsBinding
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.database.AUTH
import com.pasha.telegramclone.utilits.AppStates
import com.pasha.telegramclone.database.FOLDER_PROFILE_IMAGE
import com.pasha.telegramclone.database.REF_STORAGE_ROOT
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.USER
import com.pasha.telegramclone.utilits.downloadAndSetImage
import com.pasha.telegramclone.database.getUrlFromStorage
import com.pasha.telegramclone.database.putImageToStorage
import com.pasha.telegramclone.database.putUrlToDatabase
import com.pasha.telegramclone.utilits.replaceFragment
import com.pasha.telegramclone.utilits.restartActivity
import com.pasha.telegramclone.utilits.showToast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class SettingsFragment : BaseFragment() {

private lateinit var binding:FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = ""
        setHasOptionsMenu(true)//включили наше меню в контексте

        initFields()//инициализация текстовых блоков в окне настроек
    }

    //инициализация всех текстView в окне настроек(взятые из БД , помещённые в объект User данные, добавили во вьюшки)
    private fun initFields() {
        binding.settingsBio.text = USER.bio
        binding.settingsFullName.text = USER.fullname
        binding.settingsPhoneNumber.text = USER.phone
        binding.settingStatus.text = USER.state
        binding.settingsUsername.text = USER.username

        //переход на фрагмент изменения уникального имени пользователя
        binding.bSettingsChangeUsername.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }

        //переход на фрагмент изменения информации о пользователе
        binding.bSettingsChangeBio.setOnClickListener { replaceFragment(ChangeBioFragment()) }

        //слушатель нажатия на кнопу изменения фотографии
        binding.settingsChangePhoto.setOnClickListener { changePhotoUser() }

        //установка картинки при открытии меню настроек
        binding.settingsUserPhoto.downloadAndSetImage(USER.photoUrl)
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
                AppStates.updateState(AppStates.OFFLINE)//при выходе из аккаунта принудительно ставим статус Offline
                AUTH.signOut()//выходим из аккаунта
                restartActivity()
            }
            //при виборе "Edit name" открываем ChangeNameFragment()
            R.id.settingsMenuChangeName -> replaceFragment(ChangeNameFragment())
        }
        return true
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
                        binding.settingsUserPhoto.downloadAndSetImage(ourUrl)//установка картинки
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = ourUrl//в объект записали url нашей фотографии
                        APP_ACTIVITY.mAppDrawer.updateHeader()//обновляем информацию в Header
                    }
                }
            }


        }
    }


}