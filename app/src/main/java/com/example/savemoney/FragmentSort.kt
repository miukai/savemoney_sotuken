package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment


class FragmentSort : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.activity_sortscreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showText()

        val button = view.findViewById<Button>(R.id.sortButton)
        button.setOnClickListener {
            showText()
        }

    }

//    private val nomContext = requireContext().applicationContext






    fun showText() {
        val texts = context?.let { queryTexts(it) }
        val prices = context?.let { queryPrice(it) }

        var gogo = listOf<String>()


        for (i in texts!!.indices) {
            val price = prices!![i].toString()
            val text = texts[i]
            val yugou = ("$text:$price")
            gogo += yugou
        }
        val listView = requireView().findViewById<ListView>(R.id.sortList)
        listView.adapter = ArrayAdapter<String>(this!!.requireContext(), R.layout.sort_item, R.id.sortListItem, gogo)

    }



}