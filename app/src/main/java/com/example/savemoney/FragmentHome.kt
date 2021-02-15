package com.example.savemoney

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentHome : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return  inflater.inflate(R.layout.activiti_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //メモ編集します。メモを変更できるようにします。
        val navController = findNavController()
        val addMemo = view.findViewById<Button>(R.id.addMemo)
        addMemo.setOnClickListener {
            navController.navigate(R.id.action_navi_home_to_navi_edit)
        }
        go()

//    DBから値を取ってきて表示
        var goalMoneydb = querygoalMoney(this!!.requireContext())
        var goalMoney = requireView().findViewById<TextView>(R.id.goalMoney)
        goalMoney.text ="${goalMoneydb[0]}円"
        var nowMoneydb = querynowMoney(this!!.requireContext())
        var nowMoney = requireView().findViewById<TextView>(R.id.nowMoney)
        nowMoney.text = "${nowMoneydb[0]}円"
        var useMoneydb = queryUsemoney(this!!.requireContext())
        var useMoney = requireView().findViewById<TextView>(R.id.useMoney)
        useMoney.text = "${useMoneydb[0]}円"

//          DBから受け取った値でHPバーを変化させる
        var bar = view.findViewById<ProgressBar>(R.id.progressBar2)
        var useMoneyInt :Int = Integer.parseInt(useMoneydb[0])
        bar.max = useMoneyInt
        var month = 1
        bar.progress = month
        var monthuseMoney = view.findViewById<TextView>(R.id.monthuseMoney)
        monthuseMoney.text = "${month}円/${useMoneydb[0]}円"

    }
//    一回だけに限定してDBにてきとーな値を入れる
    val go = once{
        val database = MemoDatabase(requireContext()).writableDatabase
        database.use {
            db->
            val record = ContentValues().apply {
                put("goalMoneydb", 3000)
                put("nowMoneydb", 1000)
                put("useMoneydb", 5000)
            }
            database.insert("setting", null, record)
        }
    }
    //    DBから受け取った値でHPバーを変化させる
    fun progreebar(view: View){
        var bar = view.findViewById<ProgressBar>(R.id.progressBar2)
        bar.max = 100
        bar.progress = 100
        var useMoney = view.findViewById<TextView>(R.id.useMoneyhome)
        var useMoneyp= view.findViewById<EditText>(R.id.useMoney)
        var useMoneypInt : Int = Integer.parseInt(useMoneyp.text.toString()).toInt()
        var maxmoney = view.findViewById<EditText>(R.id.maxMoney)
        var maxmoneyInt : Int = Integer.parseInt(maxmoney.text.toString()).toInt()
        bar.max = maxmoneyInt
        bar.progress = useMoneypInt
        val useMoneyString = useMoneyp.text.toString()
        useMoney.setText("${useMoneyString}円")
    }
}