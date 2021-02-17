package com.example.savemoney

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

class FragmentSetting : Fragment() { override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_settingscreen, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //    DBから値を取ってきて表示
        var goalMoneydb = querygoalMoney(this!!.requireContext())
        var goalMoney = requireView().findViewById<TextView>(R.id.EditgoalMoney)
        goalMoney.setText(goalMoneydb[0].toString())
        var nowMoneydb = querynowMoney(this!!.requireContext())
        var nowMoney = requireView().findViewById<TextView>(R.id.EditnowMoney)
        nowMoney.setText(nowMoneydb[0].toString())
        var useMoneydb = queryUsemoney(this!!.requireContext())
        var useMoney = requireView().findViewById<TextView>(R.id.EdituseMoney)
        useMoney.setText(useMoneydb[0].toString())

        val button = requireView().findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val  goalMoney = requireView().findViewById<EditText>(R.id.EditgoalMoney)
            val  nowMoney = requireView().findViewById<EditText>(R.id.EditnowMoney)
            val  useMoney = requireView().findViewById<EditText>(R.id.EdituseMoney)
            insertSettingMoney(this!!.requireContext(),goalMoney.text,nowMoney.text, useMoney.text)
        }
    }
}