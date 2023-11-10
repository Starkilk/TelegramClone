package com.pasha.telegramclone.database

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pasha.telegramclone.R
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.models.UserModel
import com.pasha.telegramclone.utilits.APP_ACTIVITY
import com.pasha.telegramclone.utilits.AppValueEventListener
import com.pasha.telegramclone.utilits.TYPE_MESSAGE_IMAGE
import com.pasha.telegramclone.utilits.showToast

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference//связь со Storage в Firebase
lateinit var USER: UserModel//наш юзер
lateinit var CURRENT_UID: String//уникальный идэнтификатор пользователя

const val TYPE_TEXT = "text"//тип отсылаемого сообщения


//константы в которые мы запишем данные польхователя(для работы с Database)
const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"

const val FOLDER_PROFILE_IMAGE = "profile_image"//название ветки в Firebase Storage
const val FOLDER_MESSAGE_IMAGE = "message_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

const val NODE_MESSAGES = "messages"

const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIME_STAMP = "timeStamp"
const val CHILD_IMAGE_URL = "imageUrl"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference


}


//функции высшего порядка
//inline функции не создаются и не вызываются(за место вызова "подставляется когд, который лежит в теле inline функции")функциональное програм-ие
inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTO_URL).setValue(url)//в БД поместили url нашей фотографии
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString())/*живой шаблон ste*/ }//если ошибка
}

//лямбда функция
inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl//скачали картину из бд
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString())/*живой шаблон ste*/ }//если ошибка
}

//лямбда функция
inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    //как только отработает этот слушатель, сразу отработает ЛЯМБРА function
    path.putFile(uri)//отправили картинку в БД
        .addOnSuccessListener { function() }//запустить следующую функцию
        .addOnFailureListener { showToast(it.message.toString())/*живой шаблон ste*/ }//если ошибка
}

//считываем данные из бд в объект User
inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)//добрались до данных о пользователе
        .addListenerForSingleValueEvent(AppValueEventListener {//слушатель, который смотрит информацию из БД
            USER = it.getValue(UserModel::class.java)
                ?: UserModel()//вписали в нашего USER(a) данные из бд
            if (USER.username.isEmpty()) {
                USER.username = CURRENT_UID
            }
            function()
        })
}


fun updatePhonesToDataBase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {//проверка на авторизованность
        REF_DATABASE_ROOT.child(NODE_PHONES)
            .addListenerForSingleValueEvent(AppValueEventListener {//слушатель изменений
                it.children.forEach { snapshot ->//бежим по всем контактным номерам, которые записаны в узел PHONES(по номерам ВСЕХ пользователей)
                    arrayContacts.forEach { contact ->//бежим по контактам определённого пользователя, которые считали в массив, когда запрашивали разрешение
                        if (snapshot.key == contact.phone) {//если в контактах у юзера есть номер телефона пользователя приложения
                            //нода, в которой находяятся id пользователей и контакты, которые есть у этих пользователей
                            REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                                .child(snapshot.value.toString())//id контакта из тел. книги
                                .child(CHILD_ID)
                                .setValue(snapshot.value.toString())//тоже id, только id: "fbsfshgtrth" (ребёнок id)
                                .addOnFailureListener { showToast(it.message.toString()) }

                            //записали в БД имена пользователей из тел.книги
                            REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                                .child(snapshot.value.toString())//id контакта из тел. книги
                                .child(CHILD_FULLNAME).setValue(contact.fullname)//
                                .addOnFailureListener { showToast(it.message.toString()) }
                        }

                    }
                }
            })
    }

}

//метод получет контакт и записывает его данные в модель CommonModel | ?: CommonModel() - если null, то создать пустой объект
fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

//преобразовывает полученые данные из Firebase в модель UserModel
fun DataSnapshot.getUserModel(): UserModel = this.getValue(UserModel::class.java) ?: UserModel()

//в БД видно какую структуру строит этот код
fun sendMessage(message: String, receivingUserId: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"//путь: сообщения->наш id->id собеседника
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"//путь: сообщения->id собеседника->наш id
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key//уникальный ключ для каждого сообзения

    //мапа, которую заполняем данными одного сообщения
    val mapMessage = hashMapOf<String, Any>()//ключ, значение
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()//cilde id уникальный номер сообщения
    mapMessage[CHILD_TIME_STAMP] = ServerValue.TIMESTAMP//время отправки сообщения(время берём с самого сервера)

    //мапа, где ключ - это путь, а значение - само сообщение(тоже мапа, реализованная выше)
    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

//в основной ветке пользователей меняем идэнтификатор пользователя на новый
fun updateCurrentUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
        .setValue(newUserName)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
                deleteOldUsername(newUserName)
            } else {
                showToast(it.exception?.message.toString())
            }
        }
}

//удаление старого имени пользователя из БД
private fun deleteOldUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username)
        .removeValue()//удалили старое имяя из БД
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            APP_ACTIVITY.supportFragmentManager.popBackStack()//вернулись назад по стеку
            USER.username = newUserName//в нашем объекте User изменили значение поля на новое
        }.addOnFailureListener { showToast(it.message.toString()) }
}

//функция изменения Bio
fun setBioToDatabase(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO)
        .setValue(newBio)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.bio = newBio
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }.addOnFailureListener { showToast(it.message.toString()) }
}

//изменение имени
fun setNameToDatabase(fullname: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_FULLNAME)//добрались до пункта fullname в БД
        .setValue(fullname)
        .addOnSuccessListener {//обновляем fullname
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.fullname = fullname//обновили fullname
            APP_ACTIVITY.mAppDrawer.updateHeader()//обновляем информацию в Header
            APP_ACTIVITY.supportFragmentManager.popBackStack()//вернулись назад по стэку
        }.addOnFailureListener { showToast(it.message.toString()) }
}


 fun sendMessageAsImage(receivingUserId: String, imageUrl: String, messageKey: String) {
     val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"//путь: сообщения->наш id->id собеседника
     val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"//путь: сообщения->id собеседника->наш id

     //мапа, которую заполняем данными одного сообщения
     val mapMessage = hashMapOf<String, Any>()//ключ, значение
     mapMessage[CHILD_FROM] = CURRENT_UID
     mapMessage[CHILD_TYPE] = TYPE_MESSAGE_IMAGE//тип сообщения - картинка
     mapMessage[CHILD_ID] = messageKey//cilde id уникальный номер сообщения
     mapMessage[CHILD_TIME_STAMP] = ServerValue.TIMESTAMP//время отправки сообщения(время берём с самого сервера)
     mapMessage[CHILD_IMAGE_URL] = imageUrl

     //мапа, где ключ - это путь, а значение - само сообщение(тоже мапа, реализованная выше)
     val mapDialog = hashMapOf<String, Any>()
     mapDialog["$refDialogUser/$messageKey"] = mapMessage
     mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

     REF_DATABASE_ROOT
         .updateChildren(mapDialog)
         .addOnFailureListener { showToast(it.message.toString()) }
}