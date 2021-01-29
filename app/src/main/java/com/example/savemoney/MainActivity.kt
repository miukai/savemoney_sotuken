package com.example.savemoney

//import gitandroidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
//MapFragment.OnShowCurrentDate
class MainActivity : AppCompatActivity() {
//    private var currentDate = Calendar.getInstance()

//    override fun onAddForm(c:Int) {
//        if (c == 1){
//            supportFragmentManager.beginTransaction().apply {
//                // add(追加先のid : Int, 追加したいFragment : Fragment)
//                add(R.id.linear,ProductFragment())
//                add(R.id.linear1,PriceFragment())
//                commit()
//            }
//        }else if(c == 2){
//            supportFragmentManager.beginTransaction().apply {
//                add(R.id.linear2,ProductFragment())
//                add(R.id.linear3,PriceFragment())
//                commit()
//            }
//        }else if(c == 3){
//            supportFragmentManager.beginTransaction().apply {
//                add(R.id.linear4,ProductFragment())
//                add(R.id.linear5,PriceFragment())
//                commit()
//            }
//        }
//    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView:BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }



//    private fun showCurrentDate(){
//        val dateView = findViewById<TextView>(R.id.detailDateDispOne)
//        dateView.setOnClickListener {
//            val dialog = DatePickerFragment()
//            dialog.arguments = Bundle().apply {
//                putSerializable("current",mapFragment.currentDate) }
//            dialog.show(supportFragmentManager,"calendar")
//        }
//        dateView.setText(DateFormat.format("MM月 dd日",mapFragment.currentDate.time))
//    }

    //位置情報に応じてカメラを移動させ、ズームする
    private fun zoomTo(map: GoogleMap, locations:List<LocationRecord>){
        if (locations.size == 1){
            val latLng = LatLng(locations[0].latitude, locations[0].longitude)

            val move = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
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

}