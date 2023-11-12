package com.pasha.telegramclone.ui.screens.message_recycler_view.view_holders

import com.pasha.telegramclone.ui.screens.message_recycler_view.views.MessageView

interface MessageHolder {
    fun drawMessage(view:MessageView)
}