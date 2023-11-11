package com.pasha.telegramclone.utilits

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//файл, который управляет всеми "разрешиниями" данного приложения

const val READ_CONTACTS = Manifest.permission.READ_CONTACTS//разрешение на доступ к контактам пользователя
const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO//разрешение на запись звука с микрофона
const val PERMISSION_REQUEST = 200


fun checkPermissions(permission: String):Boolean{
    //если SDK пользователя >= 23
    return if (Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(APP_ACTIVITY, permission) != PackageManager.PERMISSION_GRANTED){//и разрешение ещё не было дано

        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), PERMISSION_REQUEST)//запрашиваем разрешение у пользователя(окно разрешения)
        false//возвращаем false
    }else true//иначе версия меньше и на них не нужно спрашивать разрешения у пользователя
}