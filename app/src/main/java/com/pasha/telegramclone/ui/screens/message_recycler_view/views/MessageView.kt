package com.pasha.telegramclone.ui.screens.message_recycler_view.views

//общий интерфейс отправки сообщений

interface MessageView {
    val id:String//id
    val from:String//откуда пришло
    val timeStamp:String//время отправки

    val fileUrl:String//адрес файа
    val text:String//текст

    //переменные, которые будут отображать тип View(чтобы можно было узнать тип сообщения)
    companion object{
        //тип IMAGE это 0
        val MESSAGE_IMAGE:Int
            get() = 0
        //тип TEXT это 1
        val MESSAGE_TEXT: Int
            get() = 1

        val MESSAGE_VOICE:Int
            get() = 2
    }

    //возвращает тип View
    fun getTypeView():Int

}