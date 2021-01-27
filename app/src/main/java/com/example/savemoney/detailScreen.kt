package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
//import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class detailScreen : Fragment() {
    var setDate = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_detail, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_unsort()//未振り分けをlistに入れる
        list_consumption()//消費をlistに入れる
        list_extravagance()//浪費をlistに入れる

        val args = arguments
        if (args != null) {
            val setDate = args.getString("Counter")
            val str = "$setDate"
            val textView = view.findViewById<TextView>(R.id.detailDate)
            textView.text = str
        }
        // カレンダー画面に戻る
        val pop01 = view.findViewById<Button>(R.id.backCal)
        pop01.setOnClickListener { v: View? ->
            val fragmentManager = fragmentManager
            fragmentManager?.popBackStack()
        }
    }

//    未振り分けをlistに入れる
    fun list_unsort(){
        val text = queryunsort(this!!.requireContext())
        val listView = requireView().findViewById<ListView>(R.id.unsortDisplay)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(),R.layout.list_detail_row,R.id.detailText1,text)
    }
    //    消費をlistに入れる
    fun list_consumption(){
        val text = queryconsumption(this!!.requireContext())
        val listView = requireView().findViewById<ListView>(R.id.consumptionDisplay)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(),R.layout.list_detail_row,R.id.detailText1,text)
    }
    //    消費をlistに入れる
    fun list_extravagance(){
        val text = queryextravagance(this!!.requireContext())
        val listView = requireView().findViewById<ListView>(R.id.extravaganceDisplay)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(),R.layout.list_detail_row,R.id.detailText1,text)
    }

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