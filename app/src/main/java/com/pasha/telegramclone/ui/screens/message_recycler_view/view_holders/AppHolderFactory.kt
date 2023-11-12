package com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.R
import com.pasha.telegramclone.ui.screens.message_recycler_view.views.MessageView

//фабричный метод
class AppHolderFactory {
    companion object{
        //надувает нужную нам разметку в зависимости от типа переданного сообщения
        fun getHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder{
            return when(viewType){
                MessageView.MESSAGE_IMAGE-> {//разметка item_image
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_image, parent, false)
                    HolderImageMessage(view)
                }

                MessageView.MESSAGE_VOICE-> {//разметка item_voice
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_voice, parent, false)
                    HolderVoiceMessage(view)
                }

                else->{//разметка item_text
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_text, parent, false)
                    HolderTextMessage(view)
                }
            }
        }
    }
}