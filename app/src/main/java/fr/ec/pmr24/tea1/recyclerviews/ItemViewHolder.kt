package fr.ec.pmr24.tea1.recyclerviews

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import fr.ec.pmr24.tea1.data.classes.ItemToDo
import fr.ec.pmr24.tea1.R
import fr.ec.pmr24.tea1.data.sql.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val itemCheckBox = itemView.findViewById<CheckBox>(R.id.item_checkbox)

    fun bind(item: ItemToDo, onCheckedChangeListener: (String, Boolean) -> Unit) {
        itemCheckBox.text = item.label
        itemCheckBox.isChecked = (item.checked == 1)

        itemCheckBox.setOnCheckedChangeListener{ _, isChecked ->
            onCheckedChangeListener(item.id, isChecked)
        }

        itemCheckBox.setOnClickListener {

            if (!item.url.isNullOrEmpty() && Patterns.WEB_URL.matcher(item.url).matches()) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(item.url)
                itemView.context.startActivity(intent)
            }
        }

    }
}