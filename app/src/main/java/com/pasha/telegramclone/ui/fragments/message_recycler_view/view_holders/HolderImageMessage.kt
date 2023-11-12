package com.pasha.telegramclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.databinding.MessageItemImageBinding

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
}