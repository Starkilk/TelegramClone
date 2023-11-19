package com.pasha.telegramclone.ui.screens.main_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pasha.telegramclone.R
import com.pasha.telegramclone.databinding.MainListItemBinding
import com.pasha.telegramclone.models.CommonModel
import com.pasha.telegramclone.utilits.downloadAndSetImage
import de.hdodenhof.circleimageview.CircleImageView


class MaimListAdapter:RecyclerView.Adapter<MaimListAdapter.MainListHolder>() {

    private var listItems = mutableListOf<CommonModel>()

    class MainListHolder(view:View):RecyclerView.ViewHolder(view){
        private val binding = MainListItemBinding.bind(view)
        val itemName:TextView = binding.mainListName
        val itemLastMessage:TextView = binding.mainListLastMessage
        val itemPhoto:CircleImageView = binding.mainListPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return MainListHolder(view)
    }

    override fun getItemCount(): Int  = listItems.size

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoUrl)
    }

    fun updateListItems(item:CommonModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

}