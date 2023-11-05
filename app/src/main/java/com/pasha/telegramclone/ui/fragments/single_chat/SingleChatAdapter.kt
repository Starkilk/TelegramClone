package com.pasha.telegramclone.ui.fragments.single_chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.MessageItemBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.utilits.asTime


class SingleChatAdapter: RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = emptyList<CommonModel>()

    class SingleChatHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = MessageItemBinding.bind(view)

        //проинициализировали вьюшки
        //для отправляющего сообщения пользователя
        val blocUserMessage: ConstraintLayout = binding.blocUserMessage
        val chatUserMessage: TextView = binding.chatUserMessage
        val chatUserMessageTime: TextView = binding.chatUserMessageTime

        //для принимающего сообщения пользователя
        val blocReceivedMessage:ConstraintLayout = binding.blocReceivedMessage
        val chatReceiveMessage:TextView = binding.chatReceivedMessage
        val chatReceiveMessageTime:TextView = binding.chatReceivedMessageTime

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size//передали размер нашего списка

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if(mListMessagesCache[position].from == CURRENT_UID){//если сообщение от текущего пользователя(от нас)
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE

            holder.chatUserMessage.text = mListMessagesCache[position].text//присваиваем текст сообщения
            holder.chatUserMessageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()//присваиваем время

        }else{//если сообщение от собеседника
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE

            holder.chatReceiveMessage.text = mListMessagesCache[position].text//присваиваем текст сообщения
            holder.chatReceiveMessageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()//присваиваем время
        }

    }

    //функция копирует список, который мы приняли в список с которым работает наш Адаптер
    @SuppressLint("NotifyDataSetChanged")
    fun setList(list:List<CommonModel>){
        mListMessagesCache = list
        notifyDataSetChanged()//говорим адаптеру, что данные изменены и их надо обработать
    }
}


