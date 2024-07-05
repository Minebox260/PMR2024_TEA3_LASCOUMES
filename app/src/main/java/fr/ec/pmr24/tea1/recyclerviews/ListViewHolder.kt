package fr.ec.pmr24.tea1.recyclerviews

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.ec.pmr24.tea1.data.classes.ListeToDo
import fr.ec.pmr24.tea1.R
import fr.ec.pmr24.tea1.ShowListActivity

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titreTextView = itemView.findViewById<TextView>(R.id.list_name)

    fun bind(list: ListeToDo) {
        titreTextView.text = list.label

        titreTextView.setOnClickListener {
            val context = titreTextView.context
            val intent = Intent(context, ShowListActivity::class.java)
            intent.putExtra("list_id", list.id)
            context.startActivity(intent)

        }
    }
}