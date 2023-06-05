package com.onurkeskin.demodemobitirmeproje.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import kotlinx.android.synthetic.main.houses_row.view.*

class HousesRecyclerViewAdapter(private val houseList:ArrayList<HouseModel>, private val listener:Listener) : RecyclerView.Adapter<HousesRecyclerViewAdapter.HouseHolder>(){
    interface Listener{
        fun onHouseItemClick(houseModel: HouseModel)

        fun onHouseOwnerItemClick(houseModel: HouseModel)

        fun onCustomerItemClick(houseModel: HouseModel)
    }
    private val colors:Array<String> = arrayOf("#4CFF33","#33FFF9","#F4FC04")
    // diğer renkler -> ,"#FF334A","#FF9933","#D433FF"
    class HouseHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(houseModel: HouseModel, colors:Array<String>, position: Int, listener:Listener){
            //customerModel'e tıklanabilir kısmı eklenecek
            itemView.setOnClickListener {
                listener.onHouseItemClick(houseModel)
            }
            /*
            itemView.housesRecyclerViewOwnerIdInfo.setOnClickListener {
                listener.onHouseOwnerItemClick(houseModel)
            }

             */

            itemView.setBackgroundColor(Color.parseColor(colors[position%3]))

            itemView.housesRecyclerViewHouseAddress.text = "Adres : " + houseModel.houseAddress
            itemView.housesRecyclerViewRent.text = "Kira : " + houseModel.rent.toString()
            itemView.housesRecyclerViewHeatResource.text ="Yakıt Türü : " + houseModel.heatResource

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.houses_row,parent,false)
        return HouseHolder(view)
    }

    override fun onBindViewHolder(holder: HouseHolder, position: Int) {
        holder.bind(houseList[position],colors,position,listener)
    }

    override fun getItemCount(): Int {
        return houseList.size
    }
}