package com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.databinding.MessageItemImageBinding
import com.pasha.telegramclone.ui.screens.message_recycler_view.views.MessageView
import com.pasha.telegramclone.utilits.asTime
import com.pasha.telegramclone.utilits.downloadAndSetImage

//холдер для отправки изображений

class HolderImageMessage(view:View):RecyclerView.ViewHolder(view), MessageHolder {
    private val binding = MessageItemImageBinding.bind(view)

    ///////IMAGE
    //для отправляющего сообщения пользователя
    private val blocUserImageMessage: ConstraintLayout = binding.blocUserImageMessage
    private val chatUserImage: ImageView = binding.chatUserImage
    private val chatUserImageMessageTime: TextView = binding.chatUserImageMessageTime

    //для принимающего сообщения пользователя
    private val blocReceivedImageMessage: ConstraintLayout = binding.blocReceivedImageMessage
    private val chatReceivedImage: ImageView = binding.chatReceivedImage
    private val chatReceivedImageMessageTime: TextView = binding.chatReceivedImageMessageTime


    //пользователь/пользователю отправил/отправили КАРТИНКУ
    override fun drawMessage(view: MessageView) {
        if(view.from == CURRENT_UID){//если сообщение от текущего пользователя(от нас)
            //отрисовали сообщение отправленое НАМИ
            blocReceivedImageMessage.visibility = View.GONE//скрыли левое View
            blocUserImageMessage.visibility = View.VISIBLE//отрисовали ПРАВОЕ View
            chatUserImage.downloadAndSetImage(view.fileUrl)//передали картинку, которую нужно отправить в чат
            chatUserImageMessageTime.text = view.timeStamp.asTime()//присваиваем время

        }else{//если сообщение от собеседника
            //отрисовали сообщение отправленое СОБЕСЕДНИКОМ
            blocReceivedImageMessage.visibility = View.VISIBLE//отрисовали ЛЕВОЕ View
            blocUserImageMessage.visibility = View.GONE//скрыли правое View
            chatReceivedImage.downloadAndSetImage(view.fileUrl)//передали картинку, которую нужно отправить в чат
            chatReceivedImageMessageTime.text = view.timeStamp.asTime()//присваиваем время
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetach() {

    }
}