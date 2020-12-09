package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

class PriceFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_price, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val price = view.findViewById<EditText>(R.id.price)
//        val priceId = Integer.parseInt(price.text.toString())
//        val listner = context as? OnPrice
//        listner?.onPrice(priceId)
//    }
    interface OnPrice{
        fun onPrice(price:Int)
    }
}
fun newPriceFragment():PriceFragment{
    val fragment = PriceFragment()
    return fragment
}
