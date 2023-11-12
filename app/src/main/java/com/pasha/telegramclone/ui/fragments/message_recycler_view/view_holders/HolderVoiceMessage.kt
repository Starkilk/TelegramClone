package com.pasha.telegramclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.databinding.MessageItemImageBinding
import com.pasha.telegramclone.databinding.MessageItemVoiceBinding
import com.pasha.telegramclone.ui.fragments.message_recycler_view.views.MessageView
import com.pasha.telegramclone.utilits.asTime

//холдер для отправки изображений

class HolderVoiceMessage(view:View):RecyclerView.ViewHolder(view) {
    private val binding = MessageItemVoiceBinding.bind(view)

    ///////IMAGE
    //для отправляющего сообщения пользователя
    val blocUserVoiceMessage: ConstraintLayout = binding.blocUserVoiceMessage
    val chatUserVoiceMessageTime: TextView = binding.chatUserVoiceMessageTime

    //для принимающего сообщения пользователя
    val blocReceivedVoiceMessage: ConstraintLayout = binding.blocReceivedVoiceMessage
    val chatReceivedVoiceMessageTime: TextView = binding.chatReceivedVoiceMessageTime

    //кнопки: слушать, остановить Для Нас
    val bChatUserPlay:ImageView = binding.bChatUserPlay
    val bChatUserStop:ImageView = binding.bChatUserStop
    //кнопки: слушать, остановить Для Собеседника
    val bChatReceivedPlay:ImageView = binding.bChatReceivedPlay
    val bChatReceivedStop:ImageView = binding.bChatReceivedStop

    //пользователь/пользователю отправил/отправили ГОЛОСОВОЕ сообщение
     fun drawMessageVoice(holder: HolderVoiceMessage, view: MessageView) {
        if(view.from == CURRENT_UID){//если сообщение от текущего пользователя(от нас)
            //отрисовали сообщение отправленое НАМИ
            holder.blocReceivedVoiceMessage.visibility = View.GONE//скрыли левое View
            holder.blocUserVoiceMessage.visibility = View.VISIBLE//отрисовали ПРАВОЕ View
            holder.chatUserVoiceMessageTime.text = view.timeStamp.asTime()//присваиваем время

        }else{//если сообщение от собеседника
            //отрисовали сообщение отправленое СОБЕСЕДНИКОМ
            holder.blocReceivedVoiceMessage.visibility = View.VISIBLE//отрисовали ЛЕВОЕ View
            holder.blocUserVoiceMessage.visibility = View.GONE//скрыли правое View
            holder.chatReceivedVoiceMessageTime.text = view.timeStamp.asTime()//присваиваем время
        }
    }

}