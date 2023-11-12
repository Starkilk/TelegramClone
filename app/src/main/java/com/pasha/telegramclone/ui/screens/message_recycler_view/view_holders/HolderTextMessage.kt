package com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.databinding.MessageItemTextBinding
import com.pasha.telegramclone.ui.screens.message_recycler_view.views.MessageView
import com.pasha.telegramclone.utilits.asTime

class HolderTextMessage(view: View):RecyclerView.ViewHolder(view), MessageHolder {
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


    //пользователь/пользователю отправил/отправили ТЕКСТ
    override fun drawMessage(view: MessageView) {
        if(view.from == CURRENT_UID){//если сообщение от текущего пользователя(от нас), то рисуем  правую View
            blocUserMessage.visibility = View.VISIBLE
            blocReceivedMessage.visibility = View.GONE

            chatUserMessage.text = view.text//присваиваем текст сообщения
            chatUserMessageTime.text = view.timeStamp.asTime()//присваиваем время

        }else{//если сообщение от собеседника, рисуем левую View
            blocUserMessage.visibility = View.GONE
            blocReceivedMessage.visibility = View.VISIBLE

            chatReceivedMessage.text = view.text//присваиваем текст сообщения
            chatReceivedMessageTime.text = view.timeStamp.asTime()//присваиваем время
        }
    }
}