package com.pasha.telegramclone.utilits

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pasha.telegramclone.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

//шаблонная функция которая фозволяет вызывать Toast во фрагментах
fun showToast(message: String){
    Toast.makeText(APP_ACTIVITY,message,Toast.LENGTH_SHORT).show()
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

//метод, который сворацивает клавиатуру после подтверждения введённых данных
fun hideKeyboard(){
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken,0)
}

fun ImageView.downloadAndSetImage(url:String){
    //УСТАНОВКА КАРТИНКИ
    Picasso.get()
        .load(url)//скачиваем картинку, которую установим на аватарку
        .fit()
        .placeholder(R.drawable.default_photo)//картинка, которая установится, если нет интернета
        .into(this)//указываем в какое View устанавливать картинку
}
