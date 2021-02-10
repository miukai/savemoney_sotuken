    package com.example.savemoney

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment


class detailScreen : Fragment() {
    var setDate = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_detail, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var str = "0"
        var DayOrMonth = view.findViewById<TextView>(R.id.detailDateDispOne)

        //カレンダー画面の日付を受け取るのに必要
        val args = arguments
        if (args != null) {
            val setDate = args.getString("Counter")
            str = "$setDate"
            val textView = view.findViewById<TextView>(R.id.detailDate)
            textView.text = str
        }
        dayunsort(str)//未振り分けをlistに入れる
        dayconsumption(str)//消費をlistに入れる
        dayextravagance(str)//浪費をlistに入れる
        // カレンダー画面に戻る
        val pop01 = view.findViewById<Button>(R.id.backCal)
        pop01.setOnClickListener { v: View? ->
            val fragmentManager = fragmentManager
            fragmentManager?.popBackStack()
        }

//      その日
        val day = view.findViewById<Button>(R.id.buttonday)
        day.setOnClickListener {
            DayOrMonth.text = "1日ごとの詳細"
            dayunsort(str)//未振り分けをlistに入れる
            dayconsumption(str)//消費をlistに入れる
            dayextravagance(str)//浪費をlistに入れる
        }

//         その月
        val split = view.findViewById<Button>(R.id.buttonmonth)
        split.setOnClickListener {
            DayOrMonth.text = "月ごとの詳細"
            val split_arr = str.split("/")
            var yearmonth = ("${split_arr[0]}/${split_arr[1]}")
            monthunsort(yearmonth)//未振り分けをlistに入れる
            monthconsumption(yearmonth)//消費をlistに入れる
            monthextravagance(yearmonth)//浪費をlistに入れる
//            val monthtotal = monthtotal(this!!.requireContext(),yearmonth)
//            Toast.makeText(context, "${split_arr[1]}月に使った金額の合計は${monthtotal}円です", Toast.LENGTH_LONG).show()
        }
    }


//    １日ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
//    未振り分けをlistに入れる
    fun dayunsort(str: String) {
        val text = queryunsort(this!!.requireContext(), str)
        val listView = requireView().findViewById<ListView>(R.id.unsortDisplay)
        listView.setOnTouchListener { v, event ->
          v.parent.requestDisallowInterceptTouchEvent(true)
          false
        }
    listView.adapter = ArrayAdapter<String>(this!!.requireContext(), R.layout.list_detail_row, R.id.detailText1, text)
    }
    //    消費をlistに入れる
    fun dayconsumption(str: String) {
        val text = queryconsumption(this!!.requireContext(), str)
        val listView = requireView().findViewById<ListView>(R.id.consumptionDisplay)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(), R.layout.list_detail_row, R.id.detailText1, text)
        //合計値を入れる
        val totalPrice = consumption(this!!.requireContext(), str)
        val ptext = requireView().findViewById<TextView>(R.id.consumptionTotal)
        ptext.text = "${totalPrice.toString()}円"
    }
    //    消費をlistに入れる
    fun dayextravagance(str: String) {
        val text = queryextravagance(this!!.requireContext(), str)
        val listView = requireView().findViewById<ListView>(R.id.extravaganceDisplay)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(), R.layout.list_detail_row, R.id.detailText1, text)
        //合計値を入れる
        val totalPrice = extravagance(this!!.requireContext(), str)
        val ptext = requireView().findViewById<TextView>(R.id.extravaganceTotal)
        ptext.text = "${totalPrice.toString()}円"
    }

//    月ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
//    未振り分けをlistに入れる
fun monthunsort(yearmonth: String) {
    val text = querymonthunsort(this!!.requireContext(), yearmonth)
    val listView = requireView().findViewById<ListView>(R.id.unsortDisplay)
    listView.adapter = ArrayAdapter<String>(this!!.requireContext(), R.layout.list_detail_row, R.id.detailText1, text)
}
    //    消費をlistに入れる
    fun monthconsumption(yearmonth: String) {
        val text = querymonthconsumption(this!!.requireContext(), yearmonth)
        val listView = requireView().findViewById<ListView>(R.id.consumptionDisplay)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(), R.layout.list_detail_row, R.id.detailText1, text)
        //合計値を入れる
        val totalPrice = monthconsumption(this!!.requireContext(), yearmonth)
        val ptext = requireView().findViewById<TextView>(R.id.consumptionTotal)
        ptext.text = "${totalPrice.toString()}円"
    }
    //    浪費をlistに入れる
    fun monthextravagance(yearmonth: String) {
        val text = querymonthextravagance(this!!.requireContext(), yearmonth)
        val listView = requireView().findViewById<ListView>(R.id.extravaganceDisplay)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(), R.layout.list_detail_row, R.id.detailText1, text)
        //合計値を入れる
        val totalPrice = monthextravagance(this!!.requireContext(), yearmonth)
        val ptext = requireView().findViewById<TextView>(R.id.extravaganceTotal)
        ptext.text = "${totalPrice.toString()}円"
    }

    //カレンダー画面の日付を受け取るのに必要
    companion object {
        fun newInstance(setDate: String): detailScreen {
            // Fragemnt02 インスタンス生成
            val fragment02 = detailScreen()

            // Bundleにパラメータを設定
            val barg = Bundle()
            barg.putString("Counter", setDate)
            fragment02.arguments = barg
            return fragment02
        }
    }
}