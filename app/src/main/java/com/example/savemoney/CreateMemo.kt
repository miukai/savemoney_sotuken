package com.example.savemoney

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


class CreateMemo : Fragment() {
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

    }













}