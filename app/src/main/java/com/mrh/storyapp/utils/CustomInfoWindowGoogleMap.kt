package com.mrh.storyapp.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.mrh.storyapp.R
import com.mrh.storyapp.data.stories.ListStoryItem

class CustomInfoWindowGoogleMap(private val context: Context) : InfoWindowAdapter {
    override fun getInfoWindow(marker: Marker): View? {
        val view = (context as AppCompatActivity)
            .layoutInflater
            .inflate(R.layout.tooltip_marker, null)

        val tvOwnerStory = view.findViewById<TextView>(R.id.tv_location_owner)
        val tvDescription = view.findViewById<TextView>(R.id.tv_location_desc)
        val infoWindowData = marker.tag as ListStoryItem

        tvOwnerStory.text = infoWindowData.name
        tvDescription.text = infoWindowData.description

        return view
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}