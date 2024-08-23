package com.trios2024evdj.contactmanager.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trios2024evdj.contactmanager.databinding.ListSelectionViewHolderBinding
import com.trios2024evdj.contactmanager.models.ContactList

class ListSelectionRecyclerViewAdapter(private val lists : MutableList<ContactList>,
    val clickListener: ListSelectionRecyclerViewClickListener) :
    RecyclerView.Adapter<ListSelectionViewHolder>()
{

    // val listContacts = arrayOf("Bugs Bunny", "Daffy Duck", "Yosemite Sam")

    interface ListSelectionRecyclerViewClickListener {
        fun listItemClicked(list: ContactList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        val binding =
            ListSelectionViewHolderBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return ListSelectionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.binding.itemContact.text = lists[position].contact
        holder.binding.itemEmail.text = lists[position].email
        holder.binding.itemPhone.text = lists[position].phone
        holder.binding.itemAddress.text = lists[position].address

        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(lists[position])
        }
    }

    fun listsUpdated() {
        notifyItemInserted(lists.size-1)
    }

}