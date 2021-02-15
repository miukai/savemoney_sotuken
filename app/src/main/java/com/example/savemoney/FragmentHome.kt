package com.example.savemoney

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentHome : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return  inflater.inflate(R.layout.activiti_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        go()
        var monthtotal = monthtotal(this!!.requireContext())
//    DBから値を取ってきて表示
        var goalMoneydb = querygoalMoney(this!!.requireContext())
        var goalMoney = requireView().findViewById<TextView>(R.id.goalMoney)
        goalMoney.setText("${goalMoneydb[0]}円")
        var nowMoneydb = querynowMoney(this!!.requireContext())
        var nowMoney = requireView().findViewById<TextView>(R.id.nowMoney)
        nowMoney.setText("${nowMoneydb[0]}円")
        var useMoneydb = queryUsemoney(this!!.requireContext())
        var useMoney = requireView().findViewById<TextView>(R.id.useMoney)
        useMoney.setText("${useMoneydb[0]}円")

//          DBから受け取った値でHPバーを変化させる
        var bar = view!!.findViewById<ProgressBar>(R.id.progressBar2)
        var useMoneyInt :Int = Integer.parseInt(useMoneydb[0])
        bar.max = useMoneyInt
        var month = 1
        var nokori = useMoneyInt - monthtotal
        bar.progress = nokori
        var monthuseMoney = requireView().findViewById<TextView>(R.id.monthuseMoney)
        monthuseMoney.setText("${nokori}円/${useMoneydb[0]}円")
    }
//    一回だけに限定してDBにてきとーな値を入れる
    val go = once{
        val database = MemoDatabase(requireContext()).writableDatabase
        database.use {
            db->
            val record = ContentValues().apply {
                put("goalMoneydb", 0)
                put("nowMoneydb", 0)
                put("useMoneydb", 0)
            }
            database.insert("setting", null, record)
        }
    }
}