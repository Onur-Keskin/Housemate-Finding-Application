package com.onurkeskin.demodemobitirmeproje.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import kotlinx.android.synthetic.main.house_owner_row.view.*

class HouseOwnerRecyclerViewAdapter(private val houseOwnerList:ArrayList<HouseOwnerModel>, private val listener:Listener) : RecyclerView.Adapter<HouseOwnerRecyclerViewAdapter.HouseOwnerRowHolder>(){
    interface Listener{
        fun onItemClick(houseOwnerModel: HouseOwnerModel)
    }
    private val colors:Array<String> = arrayOf("#4CFF33","#33FFF9")
    class HouseOwnerRowHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(houseOwnerModel: HouseOwnerModel,colors:Array<String>,position: Int, listener:Listener){
            itemView.setOnClickListener {
                listener.onItemClick(houseOwnerModel)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position%2]))

            itemView.houseOwnerRecyclerViewName.text = "Name: " + houseOwnerModel.ownerName
            itemView.houseOwnerRecyclerViewSurname.text = "Surname: " + houseOwnerModel.ownerSurname
            itemView.houseOwnerRecyclerViewDepartment.text = "Department: " + houseOwnerModel.ownerDepatment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseOwnerRowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.house_owner_row,parent,false)
        return HouseOwnerRowHolder(view)
    }

    override fun onBindViewHolder(holder: HouseOwnerRowHolder, position: Int) {
        holder.bind(houseOwnerList[position],colors,position,listener)
    }

    override fun getItemCount(): Int {
        return houseOwnerList.size
    }
}