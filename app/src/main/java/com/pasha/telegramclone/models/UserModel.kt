package com.pasha.telegramclone.models

//модель данных пользователя
data class UserModel(
    val id:String = "",
    var username:String = "",
    var bio:String = "",
    var fullname:String = "",
    var state:String = "",
    var phone:String = "",
    var photoUrl:String = "empty"
)