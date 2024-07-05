package fr.ec.pmr24.tea1.recyclerviews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.ec.pmr24.tea1.data.classes.ListeToDo
import fr.ec.pmr24.tea1.R

class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {

    private var dataSource = mutableListOf<ListeToDo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list, parent, false)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun show(lists: List<ListeToDo>) {
        dataSource = lists.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataSource[position])
    }
}
