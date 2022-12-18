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
    private val colors:Array<String> = arrayOf("#4CFF33","#33FFF9")
    // diğer renkler -> ,"#FF334A","#FF9933","#D433FF"
    class HouseHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(houseModel: HouseModel, colors:Array<String>, position: Int, listener:Listener){
            //customerModel'e tıklanabilir kısmı eklenecek
            itemView.setOnClickListener {
                listener.onHouseItemClick(houseModel)
            }
            itemView.housesRecyclerViewOwnerIdInfo.setOnClickListener {
                listener.onHouseOwnerItemClick(houseModel)
            }
            itemView.housesRecyclerViewCustomerIdInfo.setOnClickListener {
                listener.onCustomerItemClick(houseModel)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position%2]))

            itemView.housesRecyclerViewHouseId.text = houseModel.houseId.toString()
            itemView.housesRecyclerViewHouseAddress.text = houseModel.houseAddress
            itemView.housesRecyclerViewRent.text = "Rent : " + houseModel.rent.toString()
            itemView.housesRecyclerViewHeatResource.text ="Heat Resource : " + houseModel.heatResource

            val size = houseModel.customerId?.size
            if(size != 0){
                itemView.housesRecyclerViewOwnerIdInfo.text = "House Owner : " + houseModel.ownerId?.get(0)
                    ?.get("ownerName").toString() + " " + houseModel.ownerId?.get(0)?.get("ownerSurname")
                    .toString()
                itemView.housesRecyclerViewCustomerIdInfo.text = "Customer : " + houseModel.customerId?.get(0)
                    ?.get("customerName").toString() + " " + houseModel.customerId?.get(0)?.get("customerSurname")
                    .toString()
            }
            else{
                itemView.housesRecyclerViewOwnerIdInfo.text = "Ev Sahini : Yok"
                itemView.housesRecyclerViewCustomerIdInfo.text = "Müşteri : Yok"
            }

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