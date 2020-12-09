package com.example.savemoney

//import gitandroidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.savemoney.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity(),EnterForm.OnButtonClickListener,MapFragment.OnShowCurrentDate,MapFragment.OnMap{
    private var currentDate = Calendar.getInstance()
    private lateinit var googleMap:GoogleMap
    
    //地図を表示
    override fun onMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.navi_map) as? SupportMapFragment
        mapFragment?.getMapAsync {
            googleMap = it
            renderMap()
        }
    }
    //日付がタップされたらカレンダーを表示
    override fun onShowCurrentDate() {
            val dialog = DatePickerFragment()
            dialog.arguments = Bundle().apply{
                putSerializable("current","currentDate")}
            dialog.show(supportFragmentManager,"calendar")
    }
    override fun onButtonClicked(){
        if (supportFragmentManager.findFragmentByTag("productFragment1")== null){
            supportFragmentManager.beginTransaction()
                    .add(R.id.container,newProductFragment(),"productFragment1")
                    .add(R.id.container, newPriceFragment(),"priceFragment1")
                    .commit()
        }else if (supportFragmentManager.findFragmentByTag("productFragment2")== null){
            supportFragmentManager.beginTransaction()
                    .add(R.id.container2,newProductFragment(),"productFragment2")
                    .add(R.id.container2,newPriceFragment(),"priceFragment2")
                    .commit()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView:BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }
    //位置情報をデータベースから検索、メソッド呼び出し
    private fun renderMap(){
        val locations = selectInDay(this,
                currentDate[Calendar.YEAR],currentDate[Calendar.MONTH],
                currentDate[Calendar.DATE])

        zoomTo(googleMap,locations)
        putMarkers(googleMap,locations)
    }
    //位置情報に応じてカメラを移動させ、ズームする
    private fun zoomTo(map: GoogleMap,locations:List<LocationRecord>){
        if (locations.size == 1){
            val latLng = LatLng(locations[0].latitude,locations[0].longitude)

            val move = CameraUpdateFactory.newLatLngZoom(latLng,15f)
            map.moveCamera(move)
        }else if (locations.size > 1){
            val bounds = LatLngBounds.Builder()
            locations.forEach{location->
                bounds.include(LatLng(location.latitude, location.longitude))
            }
            val padding = (50 * resources.displayMetrics.density).toInt()

            val move = CameraUpdateFactory.newLatLngBounds(bounds.build(),
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    padding)


            map.moveCamera(move)
        }
    }
    private fun putMarkers(map:GoogleMap,locations: List<LocationRecord>) {

    }
}