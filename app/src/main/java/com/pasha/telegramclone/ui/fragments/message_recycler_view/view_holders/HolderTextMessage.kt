package com.pasha.telegramclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.databinding.MessageItemTextBinding

class HolderTextMessage(view: View):RecyclerView.ViewHolder(view) {
    private val binding = MessageItemTextBinding.bind(view)

    //////TEXT
    //проинициализировали вьюшки
    //для отправляющего сообщения пользователя
    val blocUserMessage: ConstraintLayout = binding.blocUserMessage
    val chatUserMessage: TextView = binding.chatUserMessage
    val chatUserMessageTime: TextView = binding.chatUserMessageTime

    //для принимающего сообщения пользователя
    val blocReceivedMessage: ConstraintLayout = binding.blocReceivedMessage
    val chatReceivedMessage: TextView = binding.chatReceivedMessage
    val chatReceivedMessageTime: TextView = binding.chatReceivedMessageTime
}