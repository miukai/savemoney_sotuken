package com.example.savemoney

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapFragment : Fragment(),DatePickerDialog.OnDateSetListener,OnMapReadyCallback{
    private var currentDate = Calendar.getInstance()
    private lateinit var mapView:MapView
    private lateinit var mMap:GoogleMap
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //inflateでレイアウトファイルをビュー化
        //マップを表示させるインターフェースを呼び出す
        return inflater.inflate(R.layout.activity_map, container, false)
        }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        // 拡大縮小ボタンを表示(True)
        mMap.uiSettings.isCompassEnabled = true
        // コンパスを表示(True) 常には表示されず、地図を傾けたりした際にのみ表示される
        mMap.uiSettings.isScrollGesturesEnabled = true
        // スワイプで地図を平行移動できる
        mMap.uiSettings.isZoomGesturesEnabled = true
        // ピンチイン・アウト(二本の指で操作すると)拡大縮小できる ＰＣだとできないかも？
        mMap.uiSettings.isRotateGesturesEnabled = true
        // 二本指で操作すると地図が回転する ＰＣだとできないかも
        mMap.uiSettings.isTiltGesturesEnabled = true
        // 二本指で操作すると地図が傾く

        mMap.setOnMapClickListener(object:GoogleMap.OnMapClickListener{
            override fun onMapClick(latlng: LatLng) {
                val location = LatLng(latlng.latitude, latlng.longitude)
                mMap.addMarker(MarkerOptions().position(location))
                // タップした場所にマーカーをたてる
            }
        })
    }

    //日付がタップされたらインターフェースを呼び出す。処理はMainActivityに記述
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.navi_map)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        val dateView = view?.findViewById(R.id.date) as TextView?
        dateView?.setOnClickListener {
            val listener = context as? OnShowCurrentDate
            listener?.onShowCurrentDate()
            dateView?.setText(DateFormat.format("MM月 dd日",currentDate.time))
        }

        // Fragment を使った Nav Controller の取得
        val navController = this.findNavController()
        val button = view.findViewById<Button>(R.id.Memobutton)
        button.setOnClickListener {
            navController.navigate(R.id.action_navi_map_to_navi_create_memo)
        }
    }

    override fun onDateSet(view:DatePicker?,
                           year: Int,month:Int,dayOfMonth:Int){
        //選択中の日付を更新
        currentDate.set(year,month,dayOfMonth)
        renderMap()
        val listener = context as? OnShowCurrentDate
        listener?.onShowCurrentDate()//日付のテキストビューを更新
    }

    //内容はActivityClassと同じで、もっと最適な書き方があると思うが、時間の関係で二つ記述している
    private fun renderMap(){
        val locations = selectInDay(requireContext(),
                currentDate[Calendar.YEAR],currentDate[Calendar.MONTH],
                currentDate[Calendar.DATE])

        zoomTo(mMap, locations)
        putMarkers(mMap, locations)
    }

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

    //マップを表示させるインターフェース、処理はActivityに記述
    interface OnMap{
        fun onMap()
    }

    //日付がタップされたときのインターフェース
    interface OnShowCurrentDate{
        fun onShowCurrentDate()
    }

    interface OnTextViewClickListener{
        fun onTextViewClicked()
    }
}

//日付を選択するダイアログフラフラグメント
class DatePickerFragment : DialogFragment(),DatePickerDialog.OnDateSetListener{
    private lateinit var calendar : Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calendar = arguments?.getSerializable("current") as? Calendar
                ?:Calendar.getInstance()
    }

    //ダイアログを生成して返す
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireContext(),this,
                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DATE])
    }

    //ユーザーが日付を選択した時のコールバックイベント
    override fun onDateSet(view:DatePicker,year:Int,month:Int,day:Int){
        if (context is DatePickerDialog.OnDateSetListener){
            (context as DatePickerDialog.OnDateSetListener).onDateSet(
                    view,year,month,day
            )
        }
    }
}

//        val dateView = view.findViewById<TextView>(R.id.date)
//                dateView.setOnClickListener {
//                    val listener = context as? OnTextViewClickListener
//                    listener?.onTextViewClicked()
//                }
//        dateView.setText(DateFormat.format("MM月 dd日",currentDate.time))