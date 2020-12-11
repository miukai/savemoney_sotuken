package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
//import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment


class detailScreen : Fragment() {
    var setDate = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_detail, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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