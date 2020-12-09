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
import androidx.navigation.fragment.findNavController

//import androidx.navigation.fragment.findNavController

class  CreateMemo : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return  inflater.inflate(R.layout.create_memo, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val nowDate: LocalDate = LocalDate.now()
    val nowDateString: String = nowDate.toString()


    val ido = 0
    val kedo = 0
    val hantei :String = "未振り分け"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var nomContext = requireContext().applicationContext

//        setContentView(R.layout.create_memo)
        val navController = this.findNavController()
        val button = view.findViewById<Button>(R.id.conf_button)
        button.setOnClickListener {
            val product = view.findViewById(R.id.productName) as EditText
            val productId = product.text.toString()
            val price = view.findViewById<EditText>(R.id.price)
            val priceId = Integer.parseInt(price.text.toString())
            insertText(nomContext,productId,
                    priceId,
                    nowDateString,
                    ido,
                    kedo,
                    hantei

            )
            navController.navigate(R.id.action_navi_create_memo_to_navi_map)
        }
        //動的にViewを追加する
//        val product = view.findViewById<EditText>(R.id.productNameID)
//        val price = view.findViewById<EditText>(R.id.priceID)
        val addButton = view.findViewById<Button>(R.id.addForm)
        var c = 1
        addButton.setOnClickListener {
            if (c < 4){                val listnear = context as? OnAddForm

                listnear?.onAddForm(c)
                c += 1
            }else {
                addButton.error ="これ以上入力追加出来ません"
            }
        }
    }
    interface OnAddForm{
        fun onAddForm(c:Int)
    }
}
