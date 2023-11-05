package com.pasha.telegramclone.utilits

import androidx.recyclerview.widget.DiffUtil
import com.pasha.telegramclone.models.CommonModel

class DiffUtilCallback (
    private val oldList:List<CommonModel>,
    private val newList:List<CommonModel>
    ): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size//в функцию передали размер старого листа

    override fun getNewListSize(): Int = newList.size//в функцию передали размер нового листа

    //сравниваем элементы из двух листов паралельно(сравниваем время отправки, тк оно тоже уникально у всех сообщений)
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].timeStamp == newList[newItemPosition].timeStamp

    //метод отработает, когда areItemsTheSame выдаст true и начнётся схематическая проверка
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]//сравниваем элементы целеком(все поля на равенство)

}



