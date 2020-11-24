package com.example.savemoney

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentMap : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.activity_map, container, false)
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment を使った Nav Controller の取得
        val navController = this.findNavController()
        val button = view.findViewById<Button>(R.id.Memobutton)
        button.setOnClickListener {
            navController.navigate(R.id.action_navi_map_to_navi_create_memo)
        }

        // View を使った Nav Controller の取得
//        val button = view.findViewById<Button>(R.id.nav_button)
//        button.setOnClickListener { view ->
//            val navController = view.findNavController()
//            navController.navigate(R.id.action_oneFragment_to_twoFragment)
//        }
    }
}
