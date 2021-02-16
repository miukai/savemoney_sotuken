package com.example.savemoney

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class FragmentEditMemo:Fragment(){
    var currentDate = Calendar.getInstance()
    private lateinit var editMemoData:MutableList<EditRecord>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.edit_memo,container,false)
    }

    //        今日の年月日取得（上から年、月、日）
    var year1 = SimpleDateFormat("yyyy").format(Date()).toInt()
    var month1 = SimpleDateFormat("MM").format(Date()).toInt()
    var day1 = SimpleDateFormat("dd").format(Date()).toInt()
    var ddd2 = "${month1}月${day1}日"
    var month2 = month1 - 1
    var ddd = "${year1}/${month1}/${day1}"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
    super.onViewCreated(view, savedInstanceState)

        var textView3 = view?.findViewById<TextView>(R.id.dateView)
        if (textView3 != null) {
            textView3.text = ddd2
        }
        val dateView = view?.findViewById(R.id.dateView) as TextView

        val memoData = mutableListOf<RadioButton>()
        val radioGroup = RadioGroup(requireContext())
        val linearLayout = view.findViewById<LinearLayout>(R.id.editContainer)
        linearLayout.addView(radioGroup)
        radioSet(memoData,radioGroup)
        var text = ""
        var id = 0
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // checkedIdから、選択されたRadioButtonを取得
            val radioButton = group.findViewById<RadioButton>(checkedId)
            text = radioButton.text.toString()
            id = radioButton.id
        }
        
        val editButton = view.findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val fragmentManager = fragmentManager
            if (fragmentManager != null) {
                val fragmentTransaction = fragmentManager.beginTransaction()
                // BackStackを設定
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment, FragmentEditScreen.newInstance(text,id))
                fragmentTransaction.commit()
            }
        }


        dateView?.setOnClickListener {
            showDatePicker(memoData,radioGroup)
        }
    }

    fun showDatePicker(memoData:MutableList<RadioButton>,radioGroup:RadioGroup){
        var textView2 = view?.findViewById<TextView>(R.id.dateView)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth ->
                var str2 = "${month + 1}月${dayOfMonth}日"
                if (textView2 != null) {
                    textView2.text = str2
                }
                currentDate.set(year,month,dayOfMonth)
                radioSet(memoData,radioGroup)
            },
            year1,
            month2,
            day1)
        datePickerDialog.show()
    }

    fun radioSet(memoData:MutableList<RadioButton>,radioGroup:RadioGroup){
        radioGroup.removeAllViews()

        editMemoData = queryEdit(requireContext(),ddd)
        editMemoData.forEach { editMemo ->
            var id = editMemo.id
            val radioButton = RadioButton(requireContext())
            val shopName = editMemo.shopName
            memoData.add(radioButton)
            radioButton.id = id.toInt()
            radioButton.text = shopName
            radioButton.textSize = 24F
            radioGroup.addView(radioButton)
        }
    }

}