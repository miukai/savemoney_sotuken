package com.example.savemoney

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.text.SimpleDateFormat
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
    //        今日の年月日取得（上から年、月、日）
    var year1 = SimpleDateFormat("yyyy").format(Date()).toInt()
    var month1 = SimpleDateFormat("MM").format(Date()).toInt()
    var day1 = SimpleDateFormat("dd").format(Date()).toInt()
    //        month2はshowDatePickerで使う
    var month2 = month1 - 1
//    dddはメモ画面に渡す日付
    var ddd = "${month1}月${day1}日"

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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Map画面を開いたときに今日の日付を表示
        var textView3 = view?.findViewById<TextView>(R.id.detailDateDispOne)
        if (textView3 != null) {
            textView3.text = ddd
        }
        val dateView = view?.findViewById(R.id.detailDateDispOne) as TextView?

        mapView = view.findViewById(R.id.navi_map)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        val dateView = view?.findViewById(R.id.date) as TextView?

        dateView?.setOnClickListener {
//            日付表示変えました
            showDatePicker()
//            val listener = context as? OnShowCurrentDate
//            listener?.onShowCurrentDate()
//            dateView?.setText(DateFormat.format("MM月 dd日",currentDate.time))
        }

//        // Fragment を使った Nav Controller の取得
//        val navController = this.findNavController()
//        val button = view.findViewById<Button>(R.id.Memobutton)
//        button.setOnClickListener {
//            navController.navigate(R.id.action_navi_map_to_navi_create_memo)


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
                    var str2 = "${month + 1}月${dayOfMonth}日"
                    if (textView2 != null) {
                        textView2.text = str2
                        ddd = str2
                    }
                },
                year1,
                month1,
                day1)

        datePickerDialog.show()
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