package com.pasha.telegramclone.utilits

enum class AppStates(val state:String) {
    ONLINE("online"),
    OFFLINE("was recently"),
    TYPING("typing");

    companion object{
        //метод записывает в БД состояние, которое мы туда отправим
        fun updateState(appStates: AppStates){
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
                .setValue(appStates.state)
                .addOnSuccessListener { USER.state = appStates.state }//обновляем поле в объекте пользователя
                .addOnFailureListener { showToast(it.message.toString()) }
        }
    }
}