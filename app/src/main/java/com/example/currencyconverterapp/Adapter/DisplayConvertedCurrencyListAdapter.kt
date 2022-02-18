package com.example.currencyconverterapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.entities.CurrencyCard

internal class DisplayConvertedCurrencyListAdapter(private var itemsList: ArrayList<CurrencyCard>) :
    RecyclerView.Adapter<DisplayConvertedCurrencyListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var curruncy_name: TextView = view.findViewById(R.id.tv_currency)
        var curruncy_value: TextView = view.findViewById(R.id.tv_currency_value)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.converted_currency_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //itemsList.get(itemsList.keys.toList().get(position))
        holder.curruncy_name.text = itemsList.get(position).currency_code?.substring(3,
            itemsList.get(position).currency_code?.length!!
        )
        holder.curruncy_value.text = itemsList.get(position).converted_value
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}