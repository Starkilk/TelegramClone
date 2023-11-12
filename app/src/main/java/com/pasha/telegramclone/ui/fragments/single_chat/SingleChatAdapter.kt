package com.pasha.telegramclone.ui.fragments.single_chat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.ui.fragments.message_recycler_view.view_holders.AppHolderFactory
import com.pasha.telegramclone.ui.fragments.message_recycler_view.view_holders.HolderImageMessage
import com.pasha.telegramclone.ui.fragments.message_recycler_view.view_holders.HolderTextMessage
import com.pasha.telegramclone.ui.fragments.message_recycler_view.views.MessageView
import com.pasha.telegramclone.utilits.asTime
import com.pasha.telegramclone.utilits.downloadAndSetImage


class SingleChatAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()//получаем какое-то сообщение(text, image и т.д)
    //private lateinit var mDiffResult: DiffUtil.DiffResult//результат проверки элементов из старого и нового листов



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)//возвращаем разметку которая нам нужна(image, text и тд)
    }

    //метод присваивает объекту MessageView MESSAGE_IMAGE или MESSAGE_TEXT
    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    override fun getItemCount(): Int = mListMessagesCache.size//передали размер нашего списка

    //в зависимости от типа холдера, отрисовываем(заполняем надутую разметку) то или иное view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HolderImageMessage -> drawMessageImage(holder,position)
            is HolderTextMessage -> drawMessageText(holder,position)
            else ->{

            }
        }
    }

    //пользователь/пользователю отправил/отправили КАРТИНКУ
    private fun drawMessageImage(holder: HolderImageMessage, position: Int) {
        if(mListMessagesCache[position].from == CURRENT_UID){//если сообщение от текущего пользователя(от нас)
            //отрисовали сообщение отправленое НАМИ
            holder.blocReceivedImageMessage.visibility = View.GONE//скрыли левое View
            holder.blocUserImageMessage.visibility = View.VISIBLE//отрисовали ПРАВОЕ View
            holder.chatUserImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)//передали картинку, которую нужно отправить в чат
            holder.chatUserImageMessageTime.text = mListMessagesCache[position].timeStamp.asTime()//присваиваем время

        }else{//если сообщение от собеседника
            //отрисовали сообщение отправленое СОБЕСЕДНИКОМ
            holder.blocReceivedImageMessage.visibility = View.VISIBLE//отрисовали ЛЕВОЕ View
            holder.blocUserImageMessage.visibility = View.GONE//скрыли правое View
            holder.chatReceivedImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)//передали картинку, которую нужно отправить в чат
            holder.chatReceivedImageMessageTime.text = mListMessagesCache[position].timeStamp.asTime()//присваиваем время
        }
    }

    //пользователь/пользователю отправил/отправили ТЕКСТ
    private fun drawMessageText(holder: HolderTextMessage, position: Int) {
        if(mListMessagesCache[position].from == CURRENT_UID){//если сообщение от текущего пользователя(от нас), то рисуем  правую View
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE

            holder.chatUserMessage.text = mListMessagesCache[position].text//присваиваем текст сообщения
            holder.chatUserMessageTime.text = mListMessagesCache[position].timeStamp.asTime()//присваиваем время

        }else{//если сообщение от собеседника, рисуем левую View
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE

            holder.chatReceivedMessage.text = mListMessagesCache[position].text//присваиваем текст сообщения
            holder.chatReceivedMessageTime.text = mListMessagesCache[position].timeStamp.asTime()//присваиваем время
        }
    }

    //добавление сообщение(отправка) вних списка, когда перемещаемся к последнему отпраленному сообщению
    fun addItemToBottom(item:MessageView, onSuccess:() -> Unit){
        if (!mListMessagesCache.contains(item)){//устраняем проблему дублирования сообщений, чтобы список на список не накладывался(сравниваем объекты по id с помощью equels в модели CommonModel)

            mListMessagesCache.add(item)//добавляем элемент в список
            notifyItemInserted(mListMessagesCache.size)//говорит адаптеру обновить элемент списка по переданному номеру(последний элемент, в нашем случае)
        }
        onSuccess()//callback, говорим, что всё выполнено
    }

    ////если нужно пролистать вверх для дорисовки старых сообщений
    fun addItemToTop(item:MessageView, onSuccess:() -> Unit){
        if (!mListMessagesCache.contains(item)){//устраняем проблему дублирования сообщений, чтобы список на список не накладывался
            mListMessagesCache.add(item)//добавляем элемент в список
            mListMessagesCache.sortBy { it.timeStamp.toString() }//сортеруем список от последнего сообщения к самому старому
            notifyItemInserted(0)//говорит адаптеру обновить элемент списка по переданному номеру(первый элемент, в нашем случае, тк отсортировали от меньшего к большему)

        }
        onSuccess()//callback, говорим, что всё выполнено
    }


}


