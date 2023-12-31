package com.pasha.telegramclone.utilits

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//вынесли в отдельный класс слушатель информации
class AppValueEventListener(val onSucces:(DataSnapshot) ->Unit):ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
        onSucces(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {

    }

}