package com.example.savemoney

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker

class ToiletInfoWindowViewer(newActivity: Activity) : InfoWindowAdapter {
    private val infoWindowView: View = newActivity.layoutInflater.inflate(R.layout.layout_map_memo, null)
    override fun getInfoWindow(marker: Marker): View {
        // TextViewにTitle, Snippetをセットする.
        (infoWindowView.findViewById<View>(R.id.marker_infowindow_title) as TextView).text = marker.title
        (infoWindowView.findViewById<View>(R.id.marker_infowindow_snippet) as TextView).text = marker.snippet
        return infoWindowView
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}
