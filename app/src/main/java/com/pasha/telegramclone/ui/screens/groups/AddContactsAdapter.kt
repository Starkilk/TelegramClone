package com.pasha.telegramclone.ui.screens.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.AddContactsItemBinding
import com.pasha.telegramclone.databinding.MainListItemBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.ui.screens.single_chat.SingleChatFragment
import com.pasha.telegramclone.utilits.downloadAndSetImage
import com.pasha.telegramclone.utilits.replaceFragment
import com.pasha.telegramclone.utilits.showToast
import de.hdodenhof.circleimageview.CircleImageView


class AddContactsAdapter:RecyclerView.Adapter<AddContactsAdapter.AddContactsHolder>() {

    private var listItems = mutableListOf<CommonModel>()

    class AddContactsHolder(view:View):RecyclerView.ViewHolder(view){
        private val binding = AddContactsItemBinding.bind(view)
        val itemName:TextView = binding.addContactsName
        val itemLastMessage:TextView = binding.addContactsLastMessage
        val itemPhoto:CircleImageView = binding.addContactsPhoto
        val itemChoice:CircleImageView = binding.addContactsChoice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_contacts_item, parent, false)

        //подключили слушатель нажанития на чаты(выделение контактов для добавления их в группу)
        val holder =  AddContactsHolder(view)
        holder.itemView.setOnClickListener {
            if(listItems[holder.bindingAdapterPosition].choice){
                holder.itemChoice.visibility = View.INVISIBLE
                listItems[holder.bindingAdapterPosition].choice = false
                AddContactsFragment.listContacts.remove(listItems[holder.bindingAdapterPosition])
            }else{
                holder.itemChoice.visibility = View.VISIBLE
                listItems[holder.bindingAdapterPosition].choice = true
                AddContactsFragment.listContacts.add(listItems[holder.bindingAdapterPosition])
            }
        }
        return holder
    }

    override fun getItemCount(): Int  = listItems.size

    override fun onBindViewHolder(holder: AddContactsHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoUrl)
    }

    fun updateListItems(item:CommonModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

}