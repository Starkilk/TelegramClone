package com.pasha.telegramclone.utilits

import com.pasha.telegramclone.database.AUTH
import com.pasha.telegramclone.database.CHILD_STATE
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.NODE_USERS
import com.pasha.telegramclone.database.REF_DATABASE_ROOT
import com.pasha.telegramclone.database.USER

enum class AppStates(val state:String) {
    ONLINE("online"),
    OFFLINE("was recently"),
    TYPING("typing");

    companion object{
        //метод записывает в БД состояние, которое мы туда отправим
        fun updateState(appStates: AppStates){
            if(AUTH.currentUser != null){//если пользователь не аторизован, то не устанавливаем ему статус
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener { USER.state = appStates.state }//обновляем поле в объекте пользователя
                    .addOnFailureListener { showToast(it.message.toString()) }
            }

        }
    }
}