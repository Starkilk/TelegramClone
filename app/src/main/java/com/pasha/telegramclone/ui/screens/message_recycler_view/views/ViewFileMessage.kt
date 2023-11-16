package com.pasha.telegramclone.ui.screens.message_recycler_view.views

//View отправки изображения

data class ViewFileMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String,
    override val text: String =""//пусто, тк для imageView он не нужен
) :MessageView{
    override fun getTypeView(): Int {//возвращаем тип "картинка"
        return MessageView.MESSAGE_FILE
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }
}