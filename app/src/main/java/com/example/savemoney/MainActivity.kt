package com.example.savemoney

//import gitandroidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity(), MapFragment.OnShowCurrentDate, CreateMemo.OnAddForm{
    private var currentDate = Calendar.getInstance()
    private lateinit var mMap:GoogleMap
//    override fun onPrice(price: Int) {
//        val price = price
//        val intent = Intent(this,CreateMemo::class.java)
//        intent.putExtra("price",price)
//        startFra()
//    }

    override fun onAddForm(c:Int) {
        if (c == 1){
            supportFragmentManager.beginTransaction().apply {
                // add(追加先のid : Int, 追加したいFragment : Fragment)
                add(R.id.linear,ProductFragment())
                add(R.id.linear1,PriceFragment())
                commit()
            }
        }else if(c == 2){
            supportFragmentManager.beginTransaction().apply {
                add(R.id.linear2,ProductFragment())
                add(R.id.linear3,PriceFragment())
                commit()
            }
        }else if(c == 3){
            supportFragmentManager.beginTransaction().apply {
                add(R.id.linear4,ProductFragment())
                add(R.id.linear5,PriceFragment())
                commit()
            }
        }
    }

    //日付がタップされたらカレンダーを表示
    override fun onShowCurrentDate() {
            val dialog = DatePickerFragment()
            dialog.arguments = Bundle().apply{
                putSerializable("current", "currentDate")}
            dialog.show(supportFragmentManager, "calendar")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView:BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }


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