package com.trios2024evdj.contactmanager.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.trios2024evdj.contactmanager.databinding.ContentBookmarkInfoBinding

// 2
class BookmarkInfoWindowAdapter(context: Activity) : GoogleMap.InfoWindowAdapter {

    // 3
    private val binding = ContentBookmarkInfoBinding.inflate(context.layoutInflater)

    // 4
    override fun getInfoWindow(marker: Marker): View? {
        // This function is required, but can return null if
        // not replacing the entire info window
        return null
    }

    // 5
    override fun getInfoContents(marker: Marker): View? {
        binding.title.text = marker.title ?: ""
        binding.phone.text = marker.snippet ?: ""

        val imageView = binding.photo
        imageView.setImageBitmap((marker.tag as Bitmap))

        return binding.root
    }
}
