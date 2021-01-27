package com.example.savemoney

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentCalendar : Fragment(), CalendarView.OnDateChangeListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return  inflater.inflate(R.layout.activity_calendar, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendarView = view.findViewById<View>(R.id.calendarView) as CalendarView
        calendarView.setOnDateChangeListener(this)

        val crgs = arguments
        if (crgs != null) {
            val useMoneyString = crgs.getString("Counter2")
            val str = "$useMoneyString"
            val textView = view.findViewById<TextView>(R.id.useMoney1234)
            textView.text = str
        }
    }
    private var setDate = ""
    // 日付を押したら　の処理
    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int,
                                     dayOfMonth: Int) {
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        // 日付変更イベントを追加
      calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
          setDate = "$year/${month+1}/$dayOfMonth"



//        // Fragment を使った N  av Controller の取得
//        val navController = this.findNavController()
//            navController.navigate(R.id.action_navi_cal_to_detailScreen)
          val fragmentManager = fragmentManager
          if (fragmentManager != null) {
              val fragmentTransaction = fragmentManager.beginTransaction()
              // detailScreenを表示（setDateを渡す）
              fragmentTransaction.addToBackStack(null)
              fragmentTransaction.replace(R.id.nav_host_fragment, detailScreen.newInstance(setDate))
              fragmentTransaction.commit()
          }
        }
    }
    companion object {
        fun newInstance(useMoneyString: String): FragmentCalendar {
            // Fragemnt03 インスタンス生成
            val fragment03 = FragmentCalendar()

            // Bundleにパラメータを設定
            val darg = Bundle()
            darg.putString("Counter2", useMoneyString)
            fragment03.arguments = darg
            return fragment03
        }
    }
}