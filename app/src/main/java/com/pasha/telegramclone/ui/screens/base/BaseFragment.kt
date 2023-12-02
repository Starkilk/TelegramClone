package com.pasha.telegramclone.ui.screens.base

import androidx.fragment.app.Fragment
import com.pasha.telegramclone.utilits.APP_ACTIVITY


open class BaseFragment() : Fragment() {
    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

   /* override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
    }*/

}
