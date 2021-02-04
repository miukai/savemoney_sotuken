package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.util.zip.Inflater

class setting_fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater,container,savedInstanceState)
        return inflater.inflate(R.layout.setting2_fragment,container,false)
//        setContentView(R.layout.activity_main)
//        button.setOnClickListener {
//            // エディットテキストのテキストを取得
//            if(edit_text.text != null){
//                // 取得したテキストを TextView に張り付ける
//                text_view.text = edit_text.text.toString()
//            }
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}