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
import java.util.*
import java.text.SimpleDateFormat

//import androidx.navigation.fragment.findNavController

class  CreateMemo : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return  inflater.inflate(R.layout.create_memo, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val nowDate: LocalDate = LocalDate.now()
    val nowDateString: String = nowDate.toString()


    val ido = 0
    val kedo = 0
    val hantei :String = "見振り分け"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttoncal = view.findViewById<Button>(R.id.memoCalButton)
        buttoncal.setOnClickListener {
            showDatePicker()
        }

        val args = arguments
        if (args != null) {
            val setDateM = args.getString("Counter")
            val str = "$setDateM"
            val textView = view.findViewById<TextView>(R.id.calDate)
            textView.text = str
        }

        var nomContext = requireContext().applicationContext

//        setContentView(R.layout.create_memo)
        val button = view.findViewById<Button>(R.id.button6)
        button.setOnClickListener {
            val  editText = view.findViewById<EditText>(R.id.productNameID)
            val  editText2 = view.findViewById<EditText>(R.id.priceID)
            insertText(nomContext,editText.text.toString(),
                    Integer.parseInt(editText2.text.toString()),
                    nowDateString,
                    ido,
                    kedo,
                    hantei

            )
        }
        // Map画面に戻る
        val pop01 = view.findViewById<Button>(R.id.backMapButton)
        pop01.setOnClickListener { v: View? ->
            val fragmentManager = fragmentManager
            fragmentManager?.popBackStack()
        }
    }
    companion object {
        fun newInstance(setDateM: String): CreateMemo {
            // Fragemnt03 インスタンス生成
            val fragment03 = CreateMemo()

            // Bundleにパラメータを設定
            val carg = Bundle()
            carg.putString("Counter", setDateM)
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
                      var str2 = "${month + 1}月${dayOfMonth}日"
                      if (textView2 != null) {
                          textView2.text = str2
                      }
                  },
                  year1,
                  month2,
                  day1)
          datePickerDialog.show()
    }
}

