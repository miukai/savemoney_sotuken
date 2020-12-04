package com.example.savemoney


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.savemoney.databinding.SortItemBinding


class FragmentSort : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.activity_sortscreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var swingData = querySwing(this!!.requireContext())
        val hoge = swingData

        val listAdapter = MyListAdapter(this!!.requireContext(),hoge)

        var ID = queryID(this!!.requireContext())
        val whereClauses = "id = ?"

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
        listAdapter.getView(listView.checkedItemPosition,listView,parent = null)
        val returnSwing: MutableList<String> = listAdapter.getItem()



        val button = view.findViewById<Button>(R.id.sortButton)
        button.setOnClickListener {
            for (x in swingData.indices){
                update(this!!.requireContext(), ID[x], returnSwing[x])
            }

        }

    }




    open class MyListAdapter(context: Context ,hoge: MutableList<String>) : BaseAdapter() {

        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


        private val swing2 = hoge

        val hoge2 = hoge.size



        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view:View
            val binding:SortItemBinding
                binding = DataBindingUtil.inflate(inflater, R.layout.sort_item, parent, false)
                view = binding.root
                view.tag = binding


            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId) {
                    R.id.consume -> swing2[position] = "消費"
                    R.id.waste -> swing2[position] = "浪費"
                }
            }
            return view
        }

        override fun getItem(position: Int): Any {
            TODO("Not yet implemented")
        }

        fun getItem(): MutableList<String> {
            return swing2
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount() : Int {
            return hoge2
        }

    }

}
