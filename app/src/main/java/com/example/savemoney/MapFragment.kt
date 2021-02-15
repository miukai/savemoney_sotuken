package com.example.savemoney

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import java.text.SimpleDateFormat
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import java.time.LocalDate
import java.util.*

class MapFragment : Fragment(),OnMapReadyCallback{
    var currentDate = Calendar.getInstance()
    private lateinit var mapView:MapView
    private lateinit var mMap:GoogleMap

    var lat = 35.6811323
    var lng = 139.7670182


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //inflateでレイアウトファイルをビュー化
        //マップを表示させるインターフェースを呼び出す
        return inflater.inflate(R.layout.activity_map, container, false)
        }
    //        今日の年月日取得（上から年、月、日）
    var year1 = SimpleDateFormat("yyyy").format(Date()).toInt()
    var month1 = SimpleDateFormat("MM").format(Date()).toInt()
    var day1 = SimpleDateFormat("dd").format(Date()).toInt()
    //        month2はshowDatePickerで使う
    var month2 = month1 - 1
//    dddはメモ画面に渡す日付
    var ddd = "${year1}/${month1}/${day1}"
    var ddd2 = "${month1}月${day1}日"

    var markerID1=""
    var deleteMarkerLat=0.0
    var deleteMarkerlng=0.0

    @RequiresApi(Build.VERSION_CODES.O)
    val nowDate: LocalDate = LocalDate.now()
    val nowDateString: String = nowDate.toString()

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        renderMap()
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
        var c = 0

        mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latlng: LatLng) {
                if (c == 0) {
                    lat = latlng.latitude
                    lng = latlng.longitude
                    val location = LatLng(latlng.latitude, latlng.longitude)
                    val strSnippet = "店名\n$100"
                    val marker = googleMap.addMarker(
                            // タップした場所にマーカーをたてる
                            MarkerOptions()
                                    .position(location) //  マーカーをたてる位置
                                    .title(nowDateString) //  タイトル(日付)
                                    .draggable(true)
                    )
                    markerID1 = marker.id
                    deleteMarkerLat=lat
                    deleteMarkerlng=lng
                    marker.showInfoWindow()
                    // タップした際にメモのポップアップを表示する処理
                    //緯度経度記録する。
                    insertMarkerLocations(requireContext(),lat,lng,currentDate[Calendar.YEAR],currentDate[Calendar.MONTH],
                            currentDate[Calendar.DATE])




                    marker.showInfoWindow()
                    Toast.makeText(context, "マーカーの位置を変えたい時、マーカーを長押してください。ドラッグできます!!",Toast.LENGTH_SHORT).show()
                    c += 1
                } else if (c != 0) {
                    Toast.makeText(context, "マーカーは一つのメモの記録に1本ずつマークしましょう。", Toast.LENGTH_SHORT).show()
                }
            }
        })



       mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{

           override fun onMarkerDragStart(marker: Marker) {
           }

           override fun onMarkerDrag(marker:Marker){
           }

           override fun onMarkerDragEnd(marker: Marker) {
               val location = marker.getPosition()
               lat = location.latitude
               lng = location.longitude
               val marker = googleMap.addMarker(
                       // タップした場所にマーカーをたてる
                       MarkerOptions()
                               .position(location) //  マーカーをたてる位置
                               .title(nowDateString) //  タイトル(日付)
                               .draggable(true)
               )
               marker.showInfoWindow()
           }
       })
        //マーカーをタップした時の処理
        //付けたマーカーを一度消すための処理
        mMap.setOnMarkerClickListener(object:GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker):Boolean {

                var markerID2 = marker.id

                if (markerID1 == markerID2){
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
                alertDialog.setTitle("マーカーを削除しますか？")
                alertDialog.setPositiveButton("OK") { _, _ ->
                    //マーカーを消す
                    marker.remove()
                    //テーブル上の最後に入ったデータを検索
//                    val del = selectDeleteMarker(requireContext())
                    //テーブル上の最後に入ったデータを削除
//                    val delMarkerList = arrayListOf<String>()
//                    delMarkerList += deleteMarkerLat.toString()
//                    delMarkerList += deleteMarkerlng.toString()

                    deleteMarker(requireContext(), deleteMarkerLat.toString(),deleteMarkerlng.toString())


                }
                alertDialog.setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, whichButton -> })
                alertDialog.show()}
                return true
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Map画面を開いたときに今日の日付を表示
        var textView3 = view?.findViewById<TextView>(R.id.detailDateDispOne)
        if (textView3 != null) {
            textView3.text = ddd2
        }
        val dateView = view?.findViewById(R.id.detailDateDispOne) as TextView?

        mapView = view.findViewById(R.id.navi_map)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

//        val dateView2 = view?.findViewById(R.id.date) as TextView?

        dateView?.setOnClickListener {
//            日付表示変えました
            showDatePicker()
        }



//        メモボタンを押したらdddをCreateMemoのほうに渡す処理
            val button01 = view.findViewById<Button>(R.id.Memobutton)
            button01.setOnClickListener { v: View? ->
                val fragmentManager = fragmentManager
                if (fragmentManager != null) {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.replace(R.id.nav_host_fragment, CreateMemo.newInstance(ddd))
                    fragmentTransaction.commit()
                    insertMarkerLocations(requireContext(), lat, lng, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH],
                            currentDate[Calendar.DATE])
                }
        }
    }

    //    カレンダーを表示し選択された日付を表示する
    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePicker() {
        var textView2 = view?.findViewById<TextView>(R.id.detailDateDispOne)

        val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth ->
                    var str1 = "${year}/${month + 1}/${dayOfMonth}"
                    var str2 = "${month + 1}月${dayOfMonth}日"
                    if (textView2 != null) {
                        textView2.text = str2
                        ddd = str1
                        ddd2 = str2
                    }
                    currentDate.set(year,month,dayOfMonth)
                    renderMap()
                },
                year1,
                month2,
                day1)

        datePickerDialog.show()
    }
//    override fun onDateSet(view:DatePicker?,
//                           year: Int,month:Int,dayOfMonth:Int){
//        //選択中の日付を更新
//        currentDate.set(year,month,dayOfMonth)
//        renderMap()
//        showCurrentDate()//日付のテキストビューを更新
//    }

    //内容はActivityClassと同じで、もっと最適な書き方があると思うが、時間の関係で二つ記述している
    fun renderMap(){
        val locations = selectInDay(requireContext(),
                currentDate[Calendar.YEAR],currentDate[Calendar.MONTH],
                currentDate[Calendar.DATE])

        zoomTo(mMap, locations)
        putMarkers(mMap, locations)
    }

    private fun zoomTo(map: GoogleMap,locations:List<MarkerRecord>){
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

    //吹き出しテスト
    private fun putMarkers(map:GoogleMap,locations: List<MarkerRecord>) {
        map.clear()


        locations.forEach { location ->
            val latLng = LatLng(location.latitude,location.longitude)
            val shopName = location.shopName
            val str = "${location.price}円:${location.swing}"
            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(shopName)
                    .snippet(str)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
            marker.showInfoWindow()
        }
    }



    //日付がタップされたときのインターフェース
    interface OnShowCurrentDate{
        fun onShowCurrentDate()
    }

}

////日付を選択するダイアログフラフラグメント
//class DatePickerFragment : DialogFragment(),DatePickerDialog.OnDateSetListener{
//    private lateinit var calendar : Calendar
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        calendar = arguments?.getSerializable("current") as? Calendar
//                ?:Calendar.getInstance()
//    }
//
//    //ダイアログを生成して返す
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return DatePickerDialog(requireContext(),this,
//                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
//                calendar[Calendar.DATE])
//    }
//
//    //ユーザーが日付を選択した時のコールバックイベント
//    override fun onDateSet(view:DatePicker,year:Int,month:Int,day:Int){
//        if (context is DatePickerDialog.OnDateSetListener){
//            (context as DatePickerDialog.OnDateSetListener).onDateSet(
//                    view,year,month,day
//            )
//        }
//    }
//
//}
