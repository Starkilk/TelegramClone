package com.pasha.telegramclone.ui.screens.message_recycler_view.views

import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_FILE
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_IMAGE
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_VOICE

class AppViewFactory {
    companion object{
        //из переданной Common модели формируем View отправки картинки ViewImageMessage
        fun getView(message: CommonModel):MessageView{
            return when(message.type){
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(message.id,message.from,message.timeStamp.toString(),message.fileUrl)
                TYPE_MESSAGE_VOICE -> ViewVoiceMessage(message.id,message.from,message.timeStamp.toString(),message.fileUrl)
                TYPE_MESSAGE_FILE -> ViewFileMessage(message.id,message.from,message.timeStamp.toString(),message.fileUrl,message.text)
                else-> ViewTextMessage(message.id,message.from,message.timeStamp.toString(),message.fileUrl, message.text)
            }
        }
    }
}