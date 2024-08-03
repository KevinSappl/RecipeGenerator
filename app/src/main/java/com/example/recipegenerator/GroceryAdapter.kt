package com.example.recipegenerator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroceryAdapter(
    private var groceries: MutableList<GroceryItem>,
    private val onDelete: (GroceryItem) -> Unit
) : RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>(), Filterable {

    private var filteredGroceries: MutableList<GroceryItem> = groceries
    private val selectedItems = mutableSetOf<GroceryItem>()

    class GroceryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.groceryName)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val grocery = filteredGroceries[position]
        holder.name.text = grocery.name
        holder.deleteButton.setOnClickListener {
            onDelete(grocery)
        }

        holder.itemView.setOnClickListener {
            if (selectedItems.contains(grocery)) {
                selectedItems.remove(grocery)
            } else {
                selectedItems.add(grocery)
            }
            notifyItemChanged(position)
        }

        holder.itemView.setBackgroundColor(
            if (selectedItems.contains(grocery)) {
                // Highlight color for selected items
                holder.itemView.context.getColor(R.color.colorSecondary)
            } else {
                // Default background color
                holder.itemView.context.getColor(android.R.color.transparent)
            }
        )
    }

    override fun getItemCount(): Int {
        return filteredGroceries.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                filteredGroceries = if (charString.isEmpty()) {
                    groceries
                } else {
                    groceries.filter {
                        it.name.contains(charString, true)
                    }.toMutableList()
                }
                val filterResults = FilterResults()
                filterResults.values = filteredGroceries
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filteredGroceries = filterResults?.values as MutableList<GroceryItem>
                notifyDataSetChanged()
            }
        }
    }

    fun updateData(newGroceries: MutableList<GroceryItem>) {
        groceries = newGroceries
        filteredGroceries = newGroceries
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<GroceryItem> {
        return selectedItems.toList()
    }
}
