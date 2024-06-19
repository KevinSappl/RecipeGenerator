import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipegenerator.GroceryItem
import com.example.recipegenerator.R
class GroceryAdapter(private var groceries: MutableList<GroceryItem>, private val onDelete: (GroceryItem) -> Unit) : RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>(),
    Filterable {

    private var filteredGroceries: MutableList<GroceryItem> = groceries

    class GroceryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.groceryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val grocery = filteredGroceries[position]
        holder.name.text = grocery.name
        holder.itemView.setOnClickListener {
            onDelete(grocery)
        }
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
}
