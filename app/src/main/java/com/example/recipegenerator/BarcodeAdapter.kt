package com.example.recipegenerator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BarcodeAdapter(private val barcodeList: MutableList<GroceryItem>) :
    RecyclerView.Adapter<BarcodeAdapter.BarcodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarcodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barcode, parent, false)
        return BarcodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarcodeViewHolder, position: Int) {
        holder.barcodeValue.text = barcodeList[position].name
    }

    override fun getItemCount(): Int {
        return barcodeList.size
    }

    class BarcodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val barcodeValue: TextView = itemView.findViewById(R.id.barcodeValue)
    }
}
