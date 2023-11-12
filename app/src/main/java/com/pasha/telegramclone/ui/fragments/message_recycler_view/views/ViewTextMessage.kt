package com.pasha.telegramclone.ui.fragments.message_recycler_view.views

//View отправки текстового сообщения

data class ViewTextMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String = "",//пусто, тк для textView он не нужен
    override val text: String
) :MessageView{//унаследовались от общего интерфеёса
    override fun getTypeView(): Int {//возвращаем тип "текс"
        return MessageView.MESSAGE_TEXT
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }
}