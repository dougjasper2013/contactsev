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
            val itemsHashSet = ArrayList(contactList.value as HashSet<ContactList>)
            val list = ContactList(contactList.key, itemsHashSet)
            contactLists.add(list)
        }
        return contactLists
    }

    fun saveList(list: ContactList) {
        sharedPreferences.edit().putStringSet(list.contact,
            list.email, list.phone, list.address).apply()
        lists.add(list)
        onListAdded.invoke()
    }
}