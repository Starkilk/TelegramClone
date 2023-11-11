package com.pasha.telegramclone.utilits

import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

//вся работа с записью с микрофона

@Suppress("DEPRECATION")
class AppVoiceRecorder {

    private val mMediaRecorder = MediaRecorder()//медиа рекодер(он записывает и сохраняет файл в файлы приложения)
    private lateinit var mFile: File//файл, куда сохраняем голосовые сообщения
    private lateinit var mMessageKey: String

    //начать запись в рекордер
    fun startRecord(messageKey: String) {
        try {//ловим ошибку(если она возникнет)
            mMessageKey = messageKey//ключ сообщения
            createFileForRecord()//создание файла для записи
            prepareMediaRecorder()//подготовка к записи
            mMediaRecorder.start()//запускаем запись
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    //остановить запись в рекордер
    fun stopRecord(onSuccess: (file: File, messageKey: String) -> Unit) {
        try {
            mMediaRecorder.stop()//остановили запись
            onSuccess(
                mFile,
                mMessageKey
            )//возвращаем c callback, чтобы отработал код после этой функции lambda
        } catch (e: Exception) {
            showToast(e.message.toString())
            mFile.delete()//удаляем файл, который закончился не правильно
        }
    }

    //освобождаем рекордер(при выходе из чата)
    fun releaseRecorder() {
        try {
            mMediaRecorder.release()//освобождаем рекордер
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    //создание файла для записи(в файлах приложения)
    private fun createFileForRecord() {
        mFile = File(
            APP_ACTIVITY.filesDir,
            mMessageKey
        )//указываем где создать файл и как назвать(объявили какой будет файл)
        mFile.createNewFile()//создали файл
    }

    //функция подготавливает MediaRecorder к записи
    private fun prepareMediaRecorder() {
        mMediaRecorder.reset()//сбросили его(очистили)
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)//указываем откуда будем получать данные(например, с микрофона)
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)//указываем формат данных
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)//указываем как будем кодировать файл
        mMediaRecorder.setOutputFile(mFile.absolutePath)//узазываем, где будем хранить записаный файл
        mMediaRecorder.prepare()//подготовка
    }


}