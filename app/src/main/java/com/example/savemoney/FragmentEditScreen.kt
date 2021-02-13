package com.example.savemoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentEditScreen: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return  inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id:Int = 0
        var shop:String? = ""
        val args = arguments
        if (args != null) {
            id = args.getInt("id")
            shop = args.getString("shopName")
        }

        val shopName = selectShop(requireContext(),shop)
        var date = ""
        val priceEdit = view.findViewById<EditText>(R.id.editPrice)
        val textView = view.findViewById<TextView>(R.id.editShopName)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val mustButton = view.findViewById<RadioButton>(R.id.mustButton)
        val wasteButton = view.findViewById<RadioButton>(R.id.wasteButton)
        shopName.forEach { shopUp->
            priceEdit.setText(shopUp.price.toString())
            date = shopUp.date
            textView.text = shopUp.shopName
            if(mustButton.text == shopUp.swing){
                radioGroup.check(R.id.mustButton)
            }else if (wasteButton.text == shopUp.swing){
                radioGroup.check(R.id.wasteButton)
            }else{
                radioGroup.check(R.id.noSwing)
            }
        }

//        navController.navigate(R.id.action_navi_edit_screen_to_detailScreen)
//        val navController = this.findNavController()
        val updateButton = view.findViewById<Button>(R.id.updateButton)
        updateButton.setOnClickListener {
            val price = priceEdit.text.toString().toInt()
            val radioId = radioGroup.checkedRadioButtonId
            val checkedRadioButton = view.findViewById<RadioButton>(radioId)
            val text = checkedRadioButton.text.toString()
            val shop = textView.text.toString()
            memoUpdate(requireContext(),shop,price,text,id)
            val fragmentManager = fragmentManager
            if (fragmentManager != null) {
                val fragmentTransaction = fragmentManager.beginTransaction()
                // BackStackを設定
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment, detailScreen.newInstance(date))
                fragmentTransaction.commit()
            }
        }
    }

    companion object {
        fun newInstance(setText:String,setId:Int): FragmentEditScreen {
            // Fragemnt03 インスタンス生成
            val fragment03 = FragmentEditScreen()
            val id = setId
            val text = setText
            // Bundleにパラメータを設定
            val carg = Bundle()
            carg.putInt("id",id)
            carg.putString("shopName",text)
            fragment03.arguments = carg
            return fragment03
        }
    }
}