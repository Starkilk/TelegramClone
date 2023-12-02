package com.pasha.telegramclone.models

//модель данных пользователя
data class CommonModel(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullname: String = "",
    var state: String = "",
    var phone: String = "",
    var photoUrl: String = "empty",

    //поля отвечающие за сообщения в чатах
    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timeStamp: Any = "",
    var fileUrl:String = "empty",

    var lastMessage:String = "",

var  choice: Boolean = false





) {
    override fun equals(other: Any?): Boolean {//сравнение объектовв по id
        return (other as CommonModel).id == id
    }
}