package com.pasha.telegramclone.utilits

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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