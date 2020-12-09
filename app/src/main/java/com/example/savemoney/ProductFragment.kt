package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

class ProductFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val product = view.findViewById<EditText>(R.id.productName)
//        val productId = product.text.toString()
//        val listnear = context as? OnProduct
//        listnear?.onProduct(productId)
//    }
    interface OnProduct{
        fun onProduct(product:String)
    }
}
fun newProductFragment():ProductFragment{
    val fragment = ProductFragment()
    return fragment
}

