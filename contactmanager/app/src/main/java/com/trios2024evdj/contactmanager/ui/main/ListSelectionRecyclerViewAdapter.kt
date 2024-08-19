package com.trios2024evdj.contactmanager.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trios2024evdj.contactmanager.databinding.ListSelectionViewHolderBinding

class ListSelectionRecyclerViewAdapter :
    RecyclerView.Adapter<ListSelectionViewHolder>()
{

    val listContacts = arrayOf("Bugs Bunny", "Daffy Duck", "Yosemite Sam")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        val binding =
            ListSelectionViewHolderBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return ListSelectionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.binding.itemContact.text = listContacts[position]
    }

}