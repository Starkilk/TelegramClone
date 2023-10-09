package com.pasha.telegramclone.utilits

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pasha.telegramclone.R
import com.pasha.telegramclone.activities.RegisterActivity
import com.pasha.telegramclone.ui.fragments.ChatsFragment

//шаблонная функция которая фозволяет вызывать Toast во фрагментах
fun Fragment.showToast(message: String){
    Toast.makeText(this.context,message,Toast.LENGTH_SHORT).show()
}

//шаблонная функция для переключения между АКТИВИТИ
fun AppCompatActivity.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

//шаблонная функцияя для переключения между ФРАГМЕНТАМИ ИЗ АКТИВИТИ
fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack:Boolean = true){
    //если true, тогда добавляем фрагмент в стэк(сделали для того чтобы из старовых фрагментов на активити нельзя было выйти "Назад")
    if(addStack){
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.dataContainer, fragment).commit()
    }else{//иначе не добавляем
        supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer, fragment).commit()
    }

}

//шаблонная функцияя для переключения между ФРАГМЕНТАМИ ИЗ ФРАГМЕНТА
fun Fragment.replaceFragment(fragment: Fragment){
    this.parentFragmentManager.beginTransaction()
        .addToBackStack(null)
        .replace(R.id.dataContainer,fragment).commit()
}
