package com.onurkeskin.demodemobitirmeproje.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import kotlinx.android.synthetic.main.customer_who_like_house_row.view.*
import kotlinx.android.synthetic.main.houses_row.view.*
import kotlinx.android.synthetic.main.liked_houses_row.view.*

class CustomerWhoLikeHouseRecyclerViewAdapter(private val customerList:ArrayList<CustomerModel>, private val listener:Listener) : RecyclerView.Adapter<CustomerWhoLikeHouseRecyclerViewAdapter.CustomerHolder>(){
    interface Listener{
        fun onCustomerItemClick(customerModel: CustomerModel)

        //fun onHouseOwnerItemClick(houseModel: HouseModel)

    }
    private val colors:Array<String> = arrayOf("#4CFF33","#33FFF9","#F4FC04")
    // diğer renkler -> ,"#FF334A","#FF9933","#D433FF"
    class CustomerHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(customerModel: CustomerModel, colors:Array<String>, position: Int, listener:Listener){
            //customerModel'e tıklanabilir kısmı eklenecek
            itemView.setOnClickListener {
                listener.onCustomerItemClick(customerModel)
            }
            /*
            itemView.housesRecyclerViewOwnerIdInfo.setOnClickListener {
                listener.onHouseOwnerItemClick(houseModel)
            }

             */

            itemView.setBackgroundColor(Color.parseColor(colors[position%3]))

            itemView.customerWhoLikesRecyclerViewNameSurname.text = "Name : " + customerModel.customerName + " " +customerModel.customerSurname
            itemView.customerWhoLikesRecyclerViewEmail.text = "Email : " + customerModel.customerEmail
            itemView.customerWhoLikesRecyclerViewAge.text ="Age : " + customerModel.customerAge

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_who_like_house_row,parent,false)
        return CustomerHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerHolder, position: Int) {
        holder.bind(customerList[position],colors,position,listener)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }
}