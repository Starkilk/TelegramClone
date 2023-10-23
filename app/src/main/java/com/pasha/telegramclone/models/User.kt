package com.pasha.telegramclone.models

//модель данных пользователя
data class User(
    val id:String = "",
    var username:String = "",
    var bio:String = "",
    var fullname:String = "",
    var status:String = "",
    var phone:String = "",
    var photoUrl:String = "",

)