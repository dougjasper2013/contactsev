package com.trios2024evdj.contactmanager.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.trios2024evdj.contactmanager.models.ContactList

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    lateinit var onListAdded: (() -> Unit)

    val lists: MutableList<ContactList> by lazy {
        retrieveLists()
    }

    private fun retrieveLists(): MutableList<ContactList> {
        val sharedPreferencesContents = sharedPreferences.all
        val contactLists = ArrayList<ContactList>()

        for (contactList in sharedPreferencesContents) {
            val itemsHashSet = ArrayList(contactList.value as HashSet<String>)

            val list = ContactList( itemsHashSet.toString(),
                itemsHashSet.toString(), itemsHashSet.toString(),itemsHashSet.toString() )

            contactLists.add(list)
        }
        return contactLists
    }

    fun saveList(list: ContactList) {
        sharedPreferences.edit().putStringSet(list.contact,
            list.email, list.phone, list.address)?.apply()
        lists.add(list)
        onListAdded.invoke()
    }
}

private fun SharedPreferences.Editor.putStringSet(contact: String, email: String, phone: String, address: String): SharedPreferences.Editor? {
    return null
}


