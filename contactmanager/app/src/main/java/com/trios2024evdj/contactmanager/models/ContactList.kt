package com.trios2024evdj.contactmanager.models

import android.os.Parcel
import android.os.Parcelable

class ContactList(val contact: String, val email: String,
    val phone: String, val address: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(contact)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactList> {
        override fun createFromParcel(parcel: Parcel): ContactList {
            return ContactList(parcel)
        }

        override fun newArray(size: Int): Array<ContactList?> {
            return arrayOfNulls(size)
        }
    }

}