package com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.databinding.MessageItemVoiceBinding
import com.pasha.telegramclone.ui.screens.message_recycler_view.views.MessageView
import com.pasha.telegramclone.utilits.AppVoicePlayer
import com.pasha.telegramclone.utilits.asTime

//холдер для отправки изображений

class HolderVoiceMessage(view:View):RecyclerView.ViewHolder(view), MessageHolder {
    private val binding = MessageItemVoiceBinding.bind(view)

    private val mAppVoicePlayer = AppVoicePlayer()//проигрыватель ГС

    ///////IMAGE
    //для отправляющего сообщения пользователя
    private val blocUserVoiceMessage: ConstraintLayout = binding.blocUserVoiceMessage
    private val chatUserVoiceMessageTime: TextView = binding.chatUserVoiceMessageTime

    //для принимающего сообщения пользователя
    private val blocReceivedVoiceMessage: ConstraintLayout = binding.blocReceivedVoiceMessage
    val chatReceivedVoiceMessageTime: TextView = binding.chatReceivedVoiceMessageTime

    //кнопки: слушать, остановить Для Нас
    private val bChatUserPlay:ImageView = binding.bChatUserPlay
    private val bChatUserStop:ImageView = binding.bChatUserStop
    //кнопки: слушать, остановить Для Собеседника
    private val bChatReceivedPlay:ImageView = binding.bChatReceivedPlay
    private val bChatReceivedStop:ImageView = binding.bChatReceivedStop


    //пользователь/пользователю отправил/отправили ГОЛОСОВОЕ сообщение
    override fun drawMessage(view: MessageView) {
        if(view.from == CURRENT_UID){//если сообщение от текущего пользователя(от нас)
            //отрисовали сообщение отправленое НАМИ
            blocReceivedVoiceMessage.visibility = View.GONE//скрыли левое View
            blocUserVoiceMessage.visibility = View.VISIBLE//отрисовали ПРАВОЕ View
            chatUserVoiceMessageTime.text = view.timeStamp.asTime()//присваиваем время

        }else{//если сообщение от собеседника
            //отрисовали сообщение отправленое СОБЕСЕДНИКОМ
            blocReceivedVoiceMessage.visibility = View.VISIBLE//отрисовали ЛЕВОЕ View
            blocUserVoiceMessage.visibility = View.GONE//скрыли правое View
            chatReceivedVoiceMessageTime.text = view.timeStamp.asTime()//присваиваем время
        }
    }

    override fun onAttach(view: MessageView) {//VoiceHolder в поле зрения
        mAppVoicePlayer.initPlayer()//создали экземпляр плейера
        if(view.from == CURRENT_UID){//проверка: мы написали сообщение или собеседник
            bChatUserPlay.setOnClickListener { //слушатель событий на кнопку "прослушать ГС на наше сообщение"
                bChatUserPlay.visibility = View.GONE
                bChatUserStop.visibility = View.VISIBLE//после нажатия на старт, появляется кнопка стоп
                bChatUserStop.setOnClickListener {//слушатель на кнопку "stop"
                    stop {//функция стоп сработала и мы меняем иконки
                        bChatUserStop.setOnClickListener(null)
                        bChatUserPlay.visibility = View.VISIBLE
                        bChatUserStop.visibility = View.GONE
                    }
                }
                play(view){//запускаем прослушиваение нашего ГС
                    //проигрывание закончилось, меняем кнопки
                    bChatUserPlay.visibility = View.VISIBLE
                    bChatUserStop.visibility = View.GONE

                }
            }
        }else{
            bChatReceivedPlay.setOnClickListener { //слушатель событий на кнопку "прослушать ГС на сообщение собеседника"
                bChatReceivedPlay.visibility = View.GONE
                bChatReceivedStop.visibility = View.VISIBLE//после нажатия на старт, появляется кнопка стоп
                bChatReceivedStop.setOnClickListener {//слушатель на кнопку "stop"
                    stop {//функция стоп сработала и мы меняем иконки
                        bChatReceivedStop.setOnClickListener(null)
                        bChatReceivedPlay.visibility = View.VISIBLE
                        bChatReceivedStop.visibility = View.GONE
                    }
                }
                play(view){//запускаем прослушиваение ГС собеседника
                    //проигрывание закончилось, меняем кнопки
                    bChatReceivedPlay.visibility = View.VISIBLE
                    bChatReceivedStop.visibility = View.GONE

                }
            }
        }
    }

    private fun play(view: MessageView, function: () -> Unit) {
        mAppVoicePlayer.play(view.id,view.fileUrl){//запускаем прослушиваение ГС/передали(клющ сообщения, url файла)
            function()
        }
    }

    fun stop(function: () -> Unit){
        mAppVoicePlayer.stopPlay {
            function()
        }
    }

    override fun onDetach() {//VoiceHolder вне поле зрения
        //удаляем слушатель, чтобы не занимать память
        bChatUserPlay.setOnClickListener (null)
        bChatReceivedPlay.setOnClickListener (null)
        mAppVoicePlayer.release()//уничтожили проигрыватель
    }

}