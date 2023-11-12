package com.pasha.telegramclone.ui.screens.single_chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders.AppHolderFactory
import com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders.HolderImageMessage
import com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders.HolderTextMessage
import com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders.HolderVoiceMessage
import com.pasha.telegramclone.ui.screens.message_recycler_view.views.MessageView


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
            is HolderImageMessage -> holder.drawMessageImage(holder,mListMessagesCache[position])
            is HolderTextMessage -> holder.drawMessageText(holder,mListMessagesCache[position])
            is HolderVoiceMessage -> holder.drawMessageVoice(holder,mListMessagesCache[position])
            else ->{

            }
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


