package fr.ec.pmr24.tea1.recyclerviews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.ec.pmr24.tea1.data.classes.ItemToDo
import fr.ec.pmr24.tea1.R

class ItemAdapter(private val onCheckedChangeListener: (String, Boolean) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {

    private var dataSource = mutableListOf<ItemToDo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun show(items: List<ItemToDo>) {
        dataSource = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataSource[position], onCheckedChangeListener)
    }
}
