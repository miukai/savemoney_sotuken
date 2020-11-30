package com.example.savemoney

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.RuntimeException

class EnterForm : Fragment() {
    override fun onAttach(context: Context){
        super.onAttach(context)
        if (context !is OnButtonClickListener)
            throw RuntimeException("リスナーを実装してください")
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_enterform, container, false)
        view.findViewById<Button>(R.id.button)
                .setOnClickListener {
                    val listener = context as? OnButtonClickListener
                    listener?.onButtonClicked()
                }
        return view
    }
    interface OnButtonClickListener {
        fun onButtonClicked()
    }
}