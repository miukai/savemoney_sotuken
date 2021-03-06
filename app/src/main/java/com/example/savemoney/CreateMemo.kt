package com.example.savemoney


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import android.widget.TextView
import android.widget.Toast
import java.util.*
import java.text.SimpleDateFormat
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception

//import androidx.navigation.fragment.findNavController

class  CreateMemo : Fragment() {
    private lateinit var return_btn:FloatingActionButton
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return  inflater.inflate(R.layout.create_memo, container, false)
    }


    var nowDateString: String = ""

    val mapFragment = MapFragment()

    val ido = 0
    val kedo = 0
    val hantei :String = "未振り分け"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var lat = 35.6811323
        var lon = 139.7670182

//        val markerLocation = selectOneDay(requireContext())
////
//        markerLocation.forEach {location ->
//            lat = location.latitude
//            lon = location.longitude
//        }

        val buttoncal = view.findViewById<Button>(R.id.memoCalButton)
        buttoncal.setOnClickListener {
            showDatePicker()
        }

        val args = arguments
        if (args != null) {
            lat = args.getDouble("Lat")
            lon = args.getDouble("Lng")
            val setDateM = args.getString("Counter")
            val str = "$setDateM"
            val textView = view.findViewById<TextView>(R.id.calDate)
            textView.text = setDateM
            nowDateString = setDateM.toString()
        }

        var nomContext = requireContext().applicationContext

//        setContentView(R.layout.create_memo)
        val navController = this.findNavController()
        val button = view.findViewById<Button>(R.id.conf_button)
        button.setOnClickListener {

            try {
                val shopName = view.findViewById(R.id.shopName) as EditText
                val shopId = shopName.text.toString()
                val priceUse = view.findViewById<EditText>(R.id.priceUse)
                val priceId = Integer.parseInt(priceUse.text.toString())
                insertText(nomContext,shopId,
                        priceId,
                        hantei,
                        lat,
                        lon,
                        nowDateString
                )
                navController.navigate(R.id.action_navi_create_memo_to_navi_map)
            }catch (e:Exception){
                Toast.makeText(context,"不正な入力値です。",Toast.LENGTH_SHORT).show()
            }

        }

        // Map画面に戻る
        return_btn = view.findViewById(R.id.floatingActionButton2)
        return_btn.setOnClickListener { v: View? ->
            val fragmentManager = fragmentManager
            fragmentManager?.popBackStack()
        }
    }
    companion object {
        fun newInstance(setDateM: String,lat:Double,lng:Double): CreateMemo {
            // Fragemnt03 インスタンス生成
            val fragment03 = CreateMemo()

            // Bundleにパラメータを設定
            val carg = Bundle()
            carg.putString("Counter", setDateM)
            carg.putDouble("Lat",lat)
            carg.putDouble("Lng",lng)
            fragment03.arguments = carg
            return fragment03
        }
    }
//    カレンダーを表示し選択された日付を表示する
    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePicker() {
    //        今日の年月日取得
    var year1 = SimpleDateFormat("yyyy").format(Date()).toInt()
    var month1 = SimpleDateFormat("MM").format(Date()).toInt()
    var day1 = SimpleDateFormat("dd").format(Date()).toInt()
//        月が＋１されて表示されてしまうのでマイナス１
    var month2 = month1 - 1

    var textView2 = view?.findViewById<TextView>(R.id.calDate)


    val datePickerDialog = DatePickerDialog(
                  requireContext(),
                  DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth ->
                      var str1 = "${year}/${month + 1}/${dayOfMonth}"
                      if (textView2 != null) {
                          textView2.text = str1
                          nowDateString = str1
                      }
                  },
                  year1,
                  month2,
                  day1)
          datePickerDialog.show()
    }
    interface OnAddForm{
        fun onAddForm(c:Int)
    }
}





