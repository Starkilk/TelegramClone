package com.pasha.telegramclone.utilits

import android.media.MediaPlayer

import com.pasha.telegramclone.database.getFileFromStorage
import java.io.File
import java.lang.Exception

//проигрыватель ГС
class AppVoicePlayer {
    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {
        mFile = File(APP_ACTIVITY.filesDir, messageKey)//инициализировали файл(указали где он находится, и передали название файла)
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            startPlay {
                function()
            }
        }else{//если файла нет и тд
            mFile.createNewFile()//создаём новый файл
            getFileFromStorage(mFile,fileUrl){//считываем в него  из хранилища url
                startPlay {//запускаем проигрывание
                    function()
                }
            }
        }
    }

    private fun startPlay(function: () -> Unit) {
        try {
            mMediaPlayer.setDataSource(mFile.absolutePath)//показываем проигрывателю путь
            mMediaPlayer.prepare()//подготавливаем файл
            mMediaPlayer.start()//запускаем проигрывание

            mMediaPlayer.setOnCompletionListener {//слушатель: слушает окончание проигрывания файла
                stopPlay {//запускаем остановку проигрывателя
                    function()
                }
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

     fun stopPlay(function: () -> Unit) {
        try {
            mMediaPlayer.stop()//остановили
            mMediaPlayer.reset()//очистили
            function()
        } catch (e: Exception) {
            showToast(e.message.toString())
            function()
        }
    }

    fun release() {
        mMediaPlayer.release()//удалили
    }

    fun initPlayer(){//инициализаци проигрывателя
        mMediaPlayer =MediaPlayer()
    }
}