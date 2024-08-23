package com.trios2024evdj.contactmanager.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.trios2024evdj.contactmanager.models.ContactList

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    lateinit var onListAdded: (() -> Unit)

    val lists: MutableList<ContactList> by lazy {
        retrieveLists()
    }

//    private fun retrieveLists(): MutableList<ContactList> {
//        sharedPreferences.edit().clear().apply()
//        val sharedPreferencesContents = sharedPreferences.all
//        val contactLists = ArrayList<ContactList>()
//        var list: ContactList
//        var contact: String = ""
//        var email: String = ""
//        var phone: String = ""
//        var address: String = ""
//        var index: Int = 0
//
//
//            for (contactList in sharedPreferencesContents) {
//                if (index.equals(0)) {
//                    contact = contactList.value.toString()
//                }
//
//                if (index.equals(1)) {
//                    email = contactList.value.toString()
//                }
//
//                if (index.equals(2)) {
//                    phone = contactList.value.toString()
//                }
//
//                if (index.equals(3))
//                {
//                    address = contactList.value.toString()
//                    list = ContactList(
//                        contact,
//                        email,
//                        phone,
//                        address
//                    )
//
//                    contactLists.add(list)
//
//                }
//
//                index++
//                if (index.equals(4))
//                {
//                    index = 0
//                }
//
//            }
//
//        return contactLists
//    }

    private fun retrieveLists(): MutableList<ContactList> {
        sharedPreferences.edit().clear().apply()
        val sharedPreferencesContents = sharedPreferences.all
        val contactLists = ArrayList<ContactList>()

        for (contactList in sharedPreferencesContents) {
            val itemsHashSet = ArrayList(contactList.value as HashSet<ContactList>)

            val list = ContactList( itemsHashSet.toString(),
                itemsHashSet.toString(), itemsHashSet.toString(),itemsHashSet.toString() )

            contactLists.add(list)
        }
        return contactLists
    }


    fun saveList(list: ContactList) {
        sharedPreferences.edit().putString(list.contact,list.contact).apply()
        sharedPreferences.edit().putString(list.email,list.email).apply()
        sharedPreferences.edit().putString(list.phone,list.phone).apply()
        sharedPreferences.edit().putString(list.address,list.address).apply()

        lists.add(list)
       onListAdded.invoke()
    }

//    fun saveList(list: ContactList) {
//        sharedPreferences.edit().putStringSet(list.contact,
//            list.email, list.phone, list.address)?.apply()
//        lists.add(list)
//        onListAdded.invoke()
//    }


}

//private fun SharedPreferences.Editor.putStringSet(contact: String, email: String, phone: String, address: String): SharedPreferences.Editor? {
//
//
//   return null
//}






