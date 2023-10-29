package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.R
import com.pasha.telegramclone.activities.MainActivity
import com.pasha.telegramclone.utilits.APP_ACTIVITY


open class BaseFragment() : Fragment() {
    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
    }

}
