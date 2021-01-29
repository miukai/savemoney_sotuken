package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class FragmentDetail : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.activity_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val consumeText = querySortedTexts(this.requireContext())
        val consumePrice = querySortedPrices(this.requireContext())

        val gogo = mutableListOf<String>()

        for (i in consumeText.indices) {
            val price = consumePrice[i].toString()
            val text = consumeText[i]
            val yugou = ("$text:$price")
            gogo += yugou
        }

        val listView = requireView().findViewById<ListView>(R.id.consumptionDisplay)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(), R.layout.detail_item, R.id.itemDetail, gogo)

    }
}