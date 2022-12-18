package com.onurkeskin.demodemobitirmeproje.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import kotlinx.android.synthetic.main.customer_row.view.*

class CustomerRecyclerViewAdapter(private val customerList:ArrayList<CustomerModel>, private val listener:Listener) : RecyclerView.Adapter<CustomerRecyclerViewAdapter.RowHolder>(),
    Filterable {
    interface Listener{
        fun onItemClick(customerModel: CustomerModel)
    }
    var customerListFull:ArrayList<CustomerModel> = ArrayList()
    var customerFilterList = ArrayList<CustomerModel>()
    private val colors:Array<String> = arrayOf("#4CFF33","#33FFF9")
    // diğer renkler -> ,"#FF334A","#FF9933","#D433FF"
    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(customerModel: CustomerModel, colors:Array<String>, position: Int, listener:Listener){
            //customerModel'e tıklanabilir kısmı eklenecek
            itemView.setOnClickListener {
                listener.onItemClick(customerModel)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position%2]))

            itemView.customerRecyclerViewNameSurname.text = customerModel.customerName +" "+customerModel.customerSurname
            itemView.customerRecyclerViewUserName.text =  customerModel.customerUserName
            itemView.customerRecyclerViewDepartment.text = customerModel.customerDepartment

        }
    }
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val charSearch:String = constraint.toString()
                if (charSearch.isEmpty()) {
                    customerFilterList = customerList
                } else {
                    val resultList = ArrayList<CustomerModel>()
                    for (row in customerList) {
                        //println(row)
                        //2 tane customer geliyor hangisi olacak
                        if (row.customerUserName.lowercase().contains(constraint.toString().lowercase())) {
                            //println(row.customerUserName)
                            //println(row.customerUserName.lowercase().contains(constraint.toString().lowercase()))
                            resultList.add(row)
                        }
                        else{
                            println("False")
                        }
                    }
                    customerFilterList = resultList
                    //println(customerFilterList)
                }
                val filterResults = FilterResults()
                filterResults.values = customerFilterList
                //println(filterResults.values)
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                //println(constraint)
                customerFilterList = results?.values as ArrayList<CustomerModel>
                notifyDataSetChanged()
                //println(customerFilterList)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_row,parent,false)
        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(customerList[position],colors,position,listener)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

}