package com.example.savemoney

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Transformations.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter




class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMapClickListener(object :GoogleMap.OnMapClickListener {
        // Map上の任意の場所にマーカーを立てる
            override fun onMapClick(latlng :LatLng) {
                val location = LatLng(latlng.latitude,latlng.longitude)
                mMap.addMarker(MarkerOptions().position(location))
            }
        })

        googleMap.uiSettings.isCompassEnabled = true
        // コンパスを表示

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        // 位置情報を取得するための許可
        googleMap.isMyLocationEnabled = true
        // 位置情報取得

        googleMap.isMyLocationButtonEnabled = true
        // 現在地ボタン



//        class CustomInfoWindowAdapter : InfoWindowAdapter {
//
//            // These are both view groups containing an ImageView with id "badge" and two
//            // TextViews with id "title" and "snippet".
//            private val window: View = layoutInflater.inflate(R.layout.custom_info_window, null)
//            private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)
//
//            override fun getInfoWindow(marker: Marker): View? {
//                if (options.checkedRadioButtonId != R.id.custom_info_window) {
//                    // This means that getInfoContents will be called.
//                    return null
//                }
//                render(marker, window)
//                return window
//            }
//
//            override fun getInfoContents(p0: Marker?): View {
//                TODO("Not yet implemented")
//            }
//        }



//            mMap.setInfoWindowAdapter(object :GoogleMap.InfoWindowAdapter{
//                override fun getInfoWindow(marker : Marker): View? {
//                    return null;
//                }
//
//                override fun getInfoContents(p0: Marker?): View {
//                    TODO("Not yet implemented")
//                }
//            })



        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


}