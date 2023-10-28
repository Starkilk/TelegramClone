package com.pasha.telegramclone.utilits

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.models.User

lateinit var AUTH:FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference//связь со Storage в Firebase
lateinit var USER:User//наш юзер
lateinit var CURRENT_UID:String//уникальный идэнтификатор пользователя


//константы в которые мы запишем данные польхователя(для работы с Database)
const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"

const val FOLDER_PROFILE_IMAGE = "profile_image"//название ветки в Firebase Storage

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

fun initFirebase(){
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference


}


//функции высшего порядка
//inline функции не создаются и не вызываются(за место вызова "подставляется когд, который лежит в теле inline функции")функциональное програм-ие
inline fun putUrlToDatabase(url: String,crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTO_URL).setValue(url)//в БД поместили url нашей фотографии
        .addOnSuccessListener {function()}
        .addOnFailureListener{ showToast(it.message.toString())/*живой шаблон ste*/ }//если ошибка
}

//лямбда функция
inline fun getUrlFromStorage(path: StorageReference,crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener {function(it.toString())}
        .addOnFailureListener{ showToast(it.message.toString())/*живой шаблон ste*/ }//если ошибка
}

//лямбда функция
inline fun putImageToStorage(uri: Uri, path: StorageReference,crossinline function: () -> Unit) {
    //как только отработает этот слушатель, сразу отработает ЛЯМБРА function
    path.putFile(uri)
        .addOnSuccessListener {function()}
        .addOnFailureListener{ showToast(it.message.toString())/*живой шаблон ste*/ }//если ошибка
}

//считываем данные из бд в объект User
inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)//добрались до данных о пользователе
        .addListenerForSingleValueEvent(AppValueEventListener{//слушатель, который смотрит информацию из БД
            USER = it.getValue(User::class.java) ?:User()//вписали в нашего USER(a) данные из бд
            if (USER.username.isEmpty()){
                USER.username = CURRENT_UID
            }
            function()
        })
}

 @SuppressLint("Range")
 fun initContacts() {
    if(checkPermissions(READ_CONTACTS)){//смотрит дано ли разрешение, если нет, то справшивает
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
            while(it.moveToNext()){
                val fullName = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))//считали имя контакта из тел. книги
                val phoneNumber = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))//считали номер контакта из книги
                val newModel = CommonModel()//создали объект нашей модели
                newModel.fullname = fullName//заполнили поля
                newModel.phone = phoneNumber.replace(Regex("[\\s,-]"),"")//заменяем пробелы и слэш на ничего
                arrayContacts.add(newModel)//добавили контакт из тел. книги в arrayListOf
            }
        }
        cursor?.close()//после считывания данных закрываем cursor
    }
}