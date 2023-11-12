package com.pasha.telegramclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.databinding.MessageItemImageBinding
import com.pasha.telegramclone.ui.fragments.message_recycler_view.views.MessageView
import com.pasha.telegramclone.utilits.asTime
import com.pasha.telegramclone.utilits.downloadAndSetImage

//холдер для отправки изображений

class HolderImageMessage(view:View):RecyclerView.ViewHolder(view) {
    private val binding = MessageItemImageBinding.bind(view)

    ///////IMAGE
    //для отправляющего сообщения пользователя
    val blocUserImageMessage: ConstraintLayout = binding.blocUserImageMessage
    val chatUserImage: ImageView = binding.chatUserImage
    val chatUserImageMessageTime: TextView = binding.chatUserImageMessageTime

    //для принимающего сообщения пользователя
    val blocReceivedImageMessage: ConstraintLayout = binding.blocReceivedImageMessage
    val chatReceivedImage: ImageView = binding.chatReceivedImage
    val chatReceivedImageMessageTime: TextView = binding.chatReceivedImageMessageTime

    //пользователь/пользователю отправил/отправили КАРТИНКУ
     fun drawMessageImage(holder: HolderImageMessage, view:MessageView) {
        if(view.from == CURRENT_UID){//если сообщение от текущего пользователя(от нас)
            //отрисовали сообщение отправленое НАМИ
            holder.blocReceivedImageMessage.visibility = View.GONE//скрыли левое View
            holder.blocUserImageMessage.visibility = View.VISIBLE//отрисовали ПРАВОЕ View
            holder.chatUserImage.downloadAndSetImage(view.fileUrl)//передали картинку, которую нужно отправить в чат
            holder.chatUserImageMessageTime.text = view.timeStamp.asTime()//присваиваем время

        }else{//если сообщение от собеседника
            //отрисовали сообщение отправленое СОБЕСЕДНИКОМ
            holder.blocReceivedImageMessage.visibility = View.VISIBLE//отрисовали ЛЕВОЕ View
            holder.blocUserImageMessage.visibility = View.GONE//скрыли правое View
            holder.chatReceivedImage.downloadAndSetImage(view.fileUrl)//передали картинку, которую нужно отправить в чат
            holder.chatReceivedImageMessageTime.text = view.timeStamp.asTime()//присваиваем время
        }
    }
}