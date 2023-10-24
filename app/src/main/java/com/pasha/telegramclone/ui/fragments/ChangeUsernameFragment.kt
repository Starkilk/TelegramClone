package com.pasha.telegramclone.ui.fragments

import android.os.Bundle
import android.provider.Contacts.Intents.UI
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.pasha.telegramclone.R
import com.pasha.telegramclone.activities.MainActivity
import com.pasha.telegramclone.databinding.FragmentChangeUsernameBinding
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.CHILD_FULLNAME
import com.pasha.telegramclone.utilits.CHILD_USERNAME
import com.pasha.telegramclone.utilits.NODE_USERNAMES
import com.pasha.telegramclone.utilits.NODE_USERS
import com.pasha.telegramclone.utilits.REF_DATABASE_ROOT
import com.pasha.telegramclone.utilits.UID
import com.pasha.telegramclone.utilits.USER
import com.pasha.telegramclone.utilits.showToast
import java.util.Locale


class ChangeUsernameFragment : Fragment() {
    private lateinit var binding:FragmentChangeUsernameBinding

    lateinit var mNewUsername: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //при запуске фрагмента настроек Draewer отключается
        (activity as MainActivity).mAppDrawer.disableDrawer()
    }
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        //устанавливаем в поле для ввода нового идентификатора старое имя пользователя
        binding.settingsInputUsername.setText(USER.username)
    }

    //метод который делают нам галочку в правом верхнем углу
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm,menu)//надулим разметку нашего меню

    }

    //слушатель нажатия на галочку
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    private fun change() {
        //поместили в переменную то, что пользователь ввёл
        mNewUsername = binding.settingsInputUsername.text.toString().lowercase(Locale.getDefault())
        if(mNewUsername.isEmpty()){
            showToast("the field is empty")
        }else{
            //проверка на уникальность идэнтификатора
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if(it.hasChild(mNewUsername)){
                        showToast("name is occupied")
                    }else{
                        changeUsername()
                    }
                })
        }
    }

    //создаём\добаляем новую ноду с идэнтификаторами пользователей и записываем в значение идэнтификатор
    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(UID)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    updateCurrentUsername()//обновление идэнтификатора пользователя в основной ветке
                }
            }
    }
    //в основной ветке пользователей меняем идэнтификатор пользователя на новый
    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME)
            .setValue(mNewUsername)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    deleteOldUsername()
                }else{
                    showToast(it.exception?.message.toString())
                }
            }
    }

    //удаление старого имени пользователя из БД
    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()//удалили старое имяя из БД
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    parentFragmentManager.popBackStack()//вернулись назад по стеку
                    USER.username = mNewUsername//в нашем объекте User изменили значение поляя на новое
                }else{
                    showToast(it.exception?.message.toString())
                }
            }
    }

    override fun onStop() {
        super.onStop()
        //при остановке фрагмента настроек Draewer включается
        (activity as MainActivity).mAppDrawer.enableDrawer()
    }
}