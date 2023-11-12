package com.pasha.telegramclone.ui.fragments.message_recycler_view.views

//View отправки изображения

data class ViewImageMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String,
    override val text: String =""//пусто, тк для imageView он не нужен
) :MessageView{
    override fun getTypeView(): Int {//возвращаем тип "картинка"
        return MessageView.MESSAGE_IMAGE
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }
}