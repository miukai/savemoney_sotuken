package com.example.savemoney


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast


class FragmentSort : Fragment(), View.OnClickListener {

    private lateinit var swingData:MutableList<String>



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater.inflate(R.layout.activity_sortscreen, container, false)
        //振り分けボタン
        val button = v.findViewById<Button>(R.id.sortButton)
        button.setOnClickListener(this)
        return v
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        swingData = querySwing(this!!.requireContext())
        val texts = context?.let { queryTexts(it) }
        val prices = context?.let { queryPrice(it) }
        val gogo = mutableListOf<String>()


        for (i in texts!!.indices) {
            val price = prices!![i].toString()
            val text = texts[i]
            val yugou = ("$text:$price")
            gogo += yugou
        }


        //リスト表示
        val listViews = view.findViewById<ListView>(R.id.sortList)
        listViews.adapter = ArrayAdapter(this!!.requireContext(),R.layout.sort_item,R.id.sortListItem,gogo)


        val huriwake = arrayOf("未振り分け", "消費", "浪費")
        var sortSelect = "未振り分け"
        var selectedItemId = 0


        //リストの一行をタップした時の処理
        listViews.setOnItemClickListener { parent, view, position, id ->
            //ダイアログを表示
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialog.setTitle("振り分け")
            alertDialog.setSingleChoiceItems(huriwake,-1){_,which->
                selectedItemId = which
            }
            //OKボタン押下時の処理
            alertDialog.setPositiveButton("OK",
                            DialogInterface.OnClickListener { _, _ ->
                                //振り分けの配列から選択
                                sortSelect = huriwake[selectedItemId]
                                //swingData(更新用の配列)に選択したものを入れる
                                swingData[position] = sortSelect
                            })
            //キャンセルボタン押下時の処理
            alertDialog.setNegativeButton("Cancel",
                            DialogInterface.OnClickListener { dialog, whichButton -> })
            alertDialog.show()
        }
        listViews.setOnItemLongClickListener { parent, view, position, id ->
            Toast.makeText(context, swingData[position], Toast.LENGTH_SHORT).show()
            return@setOnItemLongClickListener true
        }


    }
    //振り分けボタン押下時
    override fun onClick(v: View?) {
        var ID = queryID(this!!.requireContext())
        for (x in ID.indices) {
            update(this!!.requireContext(), ID[x], swingData[x])
        }
        Toast.makeText(context, "保存が完了しました。", Toast.LENGTH_SHORT).show()
    }

}

