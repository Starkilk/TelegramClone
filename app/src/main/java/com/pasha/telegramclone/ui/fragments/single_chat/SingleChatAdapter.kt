package com.pasha.telegramclone.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.MessageItemBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_IMAGE
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_TEXT
import com.pasha.telegramclone.utilits.asTime
import com.pasha.telegramclone.utilits.downloadAndSetImage


class SingleChatAdapter: RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = mutableListOf<CommonModel>()
    //private lateinit var mDiffResult: DiffUtil.DiffResult//результат проверки элементов из старого и нового листов

    class SingleChatHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = MessageItemBinding.bind(view)

        //////TEXT
        //проинициализировали вьюшки
        //для отправляющего сообщения пользователя
        val blocUserMessage: ConstraintLayout = binding.blocUserMessage
        val chatUserMessage: TextView = binding.chatUserMessage
        val chatUserMessageTime: TextView = binding.chatUserMessageTime

        //для принимающего сообщения пользователя
        val blocReceivedMessage:ConstraintLayout = binding.blocReceivedMessage
        val chatReceivedMessage:TextView = binding.chatReceivedMessage
        val chatReceivedMessageTime:TextView = binding.chatReceivedMessageTime


        ///////IMAGE
        //для отправляющего сообщения пользователя
        val blocUserImageMessage:ConstraintLayout = binding.blocUserImageMessage
        val chatUserImage:ImageView = binding.chatUserImage
        val chatUserImageMessageTime: TextView = binding.chatUserImageMessageTime

        //для принимающего сообщения пользователя
        val blocReceivedImageMessage:ConstraintLayout = binding.blocReceivedImageMessage
        val chatReceivedImage:ImageView = binding.chatReceivedImage
        val chatReceivedImageMessageTime: TextView = binding.chatReceivedImageMessageTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size//передали размер нашего списка

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        when(mListMessagesCache[position].type){
            TYPE_MESSAGE_TEXT -> drawMessageText(holder, position)//отрисовать текстовое сообщение
            TYPE_MESSAGE_IMAGE -> drawMessageImage(holder, position)//отрисовать image message
        }

    }

    //пользователь/пользователю отправил/отправили КАРТИНКУ
    private fun drawMessageImage(holder: SingleChatAdapter.SingleChatHolder, position: Int) {
        //отключаем блоки относящиеся к текстовуму сообщению
        holder.blocUserMessage.visibility = View.GONE
        holder.blocReceivedMessage.visibility = View.GONE
        if(mListMessagesCache[position].from == CURRENT_UID){//если сообщение от текущего пользователя(от нас)
            //отрисовали сообщение отправленое НАМИ
            holder.blocReceivedImageMessage.visibility = View.GONE//скрыли левое View
            holder.blocUserImageMessage.visibility = View.VISIBLE//отрисовали ПРАВОЕ View
            holder.chatUserImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)//передали картинку, которую нужно отправить в чат
            holder.chatUserImageMessageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()//присваиваем время

        }else{//если сообщение от собеседника
            //отрисовали сообщение отправленое СОБЕСЕДНИКОМ
            holder.blocReceivedImageMessage.visibility = View.VISIBLE//отрисовали ЛЕВОЕ View
            holder.blocUserImageMessage.visibility = View.GONE//скрыли правое View
            holder.chatReceivedImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)//передали картинку, которую нужно отправить в чат
            holder.chatReceivedImageMessageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()//присваиваем время
        }
    }

    //пользователь/пользователю отправил/отправили ТЕКСТ
    private fun drawMessageText(holder: SingleChatHolder, position: Int) {
        //отключаем блоки относящиеся к image message
        holder.blocReceivedImageMessage.visibility = View.GONE
        holder.blocUserImageMessage.visibility = View.GONE
        if(mListMessagesCache[position].from == CURRENT_UID){//если сообщение от текущего пользователя(от нас), то рисуем  правую View
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE

            holder.chatUserMessage.text = mListMessagesCache[position].text//присваиваем текст сообщения
            holder.chatUserMessageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()//присваиваем время

        }else{//если сообщение от собеседника, рисуем левую View
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE

            holder.chatReceivedMessage.text = mListMessagesCache[position].text//присваиваем текст сообщения
            holder.chatReceivedMessageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()//присваиваем время
        }
    }

    //добавление сообщение(отправка) вних списка, когда перемещаемся к последнему отпраленному сообщению
    fun addItemToBottom(item:CommonModel, onSuccess:() -> Unit){
        if (!mListMessagesCache.contains(item)){//устраняем проблему дублирования сообщений, чтобы список на список не накладывался(сравниваем объекты по id с помощью equels в модели CommonModel)

            mListMessagesCache.add(item)//добавляем элемент в список
            notifyItemInserted(mListMessagesCache.size)//говорит адаптеру обновить элемент списка по переданному номеру(последний элемент, в нашем случае)
        }
        onSuccess()//callback, говорим, что всё выполнено
    }

    ////если нужно пролистать вверх для дорисовки старых сообщений
    fun addItemToTop(item:CommonModel, onSuccess:() -> Unit){
        if (!mListMessagesCache.contains(item)){//устраняем проблему дублирования сообщений, чтобы список на список не накладывался
            mListMessagesCache.add(item)//добавляем элемент в список
            mListMessagesCache.sortBy { it.timeStamp.toString() }//сортеруем список от последнего сообщения к самому старому
            notifyItemInserted(0)//говорит адаптеру обновить элемент списка по переданному номеру(первый элемент, в нашем случае, тк отсортировали от меньшего к большему)

        }
        onSuccess()//callback, говорим, что всё выполнено
    }


}


