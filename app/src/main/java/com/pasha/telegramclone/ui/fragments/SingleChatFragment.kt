package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.DatabaseReference
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.FragmentSingleChatBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.models.UserModel
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.downloadAndSetImage
import com.pasha.telegramclone.utilits.getUserModel
import de.hdodenhof.circleimageview.CircleImageView


class SingleChatFragment(private val contact: CommonModel) : BaseFragment() {
    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var mListenerInfoToolbar:AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo:View
    private lateinit var mRefUser: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(inflater,container, false)
        return binding.root
    }

    //при открытии чата менять инфу на тулбаре
    override fun onResume() {
        super.onResume()
        mToolbarInfo =  APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbarInfo)
        mToolbarInfo.visibility = View.VISIBLE

        //слушатель изменений в БД
        mListenerInfoToolbar = AppValueEventListener {//it - пользователь из списка контактов на который мы нажали
            mReceivingUser = it.getUserModel()//записали данныен из БД в объект
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)//путь до данных пользователя, чат с которым открыли
        mRefUser.addValueEventListener(mListenerInfoToolbar)//подключили слушатель к данному пользователю
    }

    //присвоили нашему toolbar(у) информацию о пользователе (Картинка, Имя, Статус)
    private fun initInfoToolbar() {
        if (mReceivingUser.fullname.isEmpty()){//если пользователь не назвал себя
            mToolbarInfo.findViewById<TextView>(R.id.toolbarContactChatFullname).text = contact.fullname//имя из тел. книги
        }else mToolbarInfo.findViewById<TextView>(R.id.toolbarContactChatFullname).text = mReceivingUser.fullname//иначе имя из БД

        mToolbarInfo.findViewById<CircleImageView>(R.id.toolbarChatImage).downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.findViewById<TextView>(R.id.toolbarContactsChatState).text = mReceivingUser.state
    }


    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE

        //при выходе из чата отключаем слушатель данных собеседника
        mRefUser.removeEventListener(mListenerInfoToolbar)

    }


}