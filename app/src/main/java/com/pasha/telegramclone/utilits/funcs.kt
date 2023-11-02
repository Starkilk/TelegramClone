package com.pasha.telegramclone.utilits

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pasha.telegramclone.R
import com.pasha.telegramclone.models.CommonModel
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

@SuppressLint("Range")
fun initContacts() {
    if (checkPermissions(READ_CONTACTS)) {//смотрит дано ли разрешение, если нет, то справшивает
        var arrayContacts = arrayListOf<CommonModel>()
        //курсор  нужен дляя считыванияя данных из БД
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null,
        )

        cursor?.let {//считывает элементы из cursor только если  они не null
            //пока в курсоре есть следующие элементы - двигаемся
            while (it.moveToNext()) {
                val fullName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))//считали имя контакта из тел. книги
                val phoneNumber =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))//считали номер контакта из книги
                val newModel = CommonModel()//создали объект нашей модели
                newModel.fullname = fullName//заполнили поля
                newModel.phone =
                    phoneNumber.replace(Regex("[\\s,-]"), "")//заменяем пробелы и слэш на ничего
                arrayContacts.add(newModel)//добавили контакт из тел. книги в arrayListOf
            }
        }
        cursor?.close()//после считывания данных закрываем cursor\
        updatePhonesToDataBase(arrayContacts)

    }
}