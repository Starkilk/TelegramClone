package com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.database.CURRENT_UID
import com.pasha.telegramclone.database.getFileFromStorage
import com.pasha.telegramclone.databinding.MessageItemFileBinding
import com.pasha.telegramclone.ui.screens.message_recycler_view.views.MessageView
import com.pasha.telegramclone.utilits.WRITE_FILES
import com.pasha.telegramclone.utilits.asTime
import com.pasha.telegramclone.utilits.checkPermissions
import com.pasha.telegramclone.utilits.showToast
import java.io.File
import java.lang.Exception

//холдер для отправки изображений

class HolderFileMessage(view:View):RecyclerView.ViewHolder(view), MessageHolder {
    private val binding = MessageItemFileBinding.bind(view)

    ///////FILE
    //для отправляющего сообщения пользователя
    private val blocUserFileMessage: ConstraintLayout = binding.blocUserFileMessage
    private val chatUserFileMessageTime: TextView = binding.chatUserFileMessageTime
    private val chatUserFileName: TextView = binding.chatUserFilename
    private val bChatUserDownload: ImageView = binding.bChatUserDownload
    private val chatUserProgressbar: ProgressBar = binding.ChatUserProgresbar


    //для принимающего сообщения пользователя
    private val blocReceivedFileMessage: ConstraintLayout = binding.blocReceivedFileMessage
    private val chatReceivedFileMessageTime: TextView = binding.chatReceivedFileMessageTime
    private val chatReceivedFileName: TextView = binding.chatReceivedFilename
    private val bChatReceivedDownload: ImageView = binding.bChatReceivedDownload
    private val chatReceivedProgressbar: ProgressBar = binding.ChatReceivedProgresbar





    //пользователь/пользователю отправил/отправили ГОЛОСОВОЕ сообщение
    override fun drawMessage(view: MessageView) {
        if(view.from == CURRENT_UID){//если сообщение от текущего пользователя(от нас)
            //отрисовали сообщение отправленое НАМИ
            blocReceivedFileMessage.visibility = View.GONE//скрыли левое View
            blocUserFileMessage.visibility = View.VISIBLE//отрисовали ПРАВОЕ View
            chatUserFileMessageTime.text = view.timeStamp.asTime()//присваиваем время
            chatUserFileName.text = view.text

        }else{//если сообщение от собеседника
            //отрисовали сообщение отправленое СОБЕСЕДНИКОМ
            blocReceivedFileMessage.visibility = View.VISIBLE//отрисовали ЛЕВОЕ View
            blocUserFileMessage.visibility = View.GONE//скрыли правое View
            chatReceivedFileMessageTime.text = view.timeStamp.asTime()//присваиваем время
            chatReceivedFileName.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {//VoiceHolder в поле зрения
        if (view.from == CURRENT_UID){
            bChatUserDownload.setOnClickListener {clickToBtnFile(view)}
        }else{ bChatReceivedDownload.setOnClickListener {clickToBtnFile(view)} }
    }

    //нажали на картинку(скачать файл)
    private fun clickToBtnFile(view: MessageView) {
        if (view.from == CURRENT_UID){
            //меняем картинку на прогресБар
            bChatUserDownload.visibility = View.INVISIBLE
            chatUserProgressbar.visibility = View.VISIBLE
        }else{
            bChatReceivedDownload.visibility = View.INVISIBLE
            chatReceivedProgressbar.visibility = View.VISIBLE
        }
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),//куда скачать файл(путь)
            view.text//название файла
        )

        try {
            if (checkPermissions(WRITE_FILES)){//если есть разрешение
                file.createNewFile()//создаём файл
                getFileFromStorage(file,view.fileUrl){//куда скачать файл из хранилища и какой файл
                    //скачали файл на телефон и убираем прогресБар
                    if (view.from == CURRENT_UID){
                        //меняем картинку на прогресБар
                        bChatUserDownload.visibility = View.VISIBLE
                        chatUserProgressbar.visibility = View.INVISIBLE
                    }else{
                        bChatReceivedDownload.visibility = View.VISIBLE
                        chatReceivedProgressbar.visibility = View.INVISIBLE
                    }
                }
            }

        }catch (e:Exception){
            showToast(e.message.toString())
        }

    }

    override fun onDetach() {//VoiceHolder вне поле зрения
        bChatUserDownload.setOnClickListener (null)
        bChatReceivedDownload.setOnClickListener (null)
    }

}