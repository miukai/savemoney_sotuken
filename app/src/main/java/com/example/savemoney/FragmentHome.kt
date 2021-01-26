package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        var bar = view.findViewById<ProgressBar>(R.id.progressBar2)
        bar.max = 100
        bar.progress = 100
        var useMoney = view.findViewById<TextView>(R.id.useMoney)

        val button = view.findViewById<Button>(R.id.progressTestbutton)
        button.setOnClickListener {

            var useMoneyp= view.findViewById<EditText>(R.id.useMoneytest)
            var useMoneypInt : Int = Integer.parseInt(useMoneyp.text.toString()).toInt()
            var maxmoney = view.findViewById<EditText>(R.id.max)
            var maxmoneyInt : Int = Integer.parseInt(maxmoney.text.toString()).toInt()

            bar.max = maxmoneyInt
            bar.progress = useMoneypInt

            val ss = useMoneyp.text.toString()
            useMoney.setText("${ss}円")

        }
    }
}