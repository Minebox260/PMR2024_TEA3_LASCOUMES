package fr.ec.pmr24.tea1.data.sql

import android.content.Context
import android.util.Log
import fr.ec.pmr24.tea1.data.api.DataProvider

object sync {

    suspend fun items(context: Context) {
        val hash = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("hash", "")
            ?: return
        val db = AppDatabase.getDatabase(context)
        val unsyncedItems = db.itemDao().getUnsynced()

        for (sync in unsyncedItems) {
            try {
                val item = db.itemDao().getItem(sync.itemId)
                if (item != null) {
                    val response = DataProvider.putItem(hash, sync.listId, item.id, item.checked)
                    if (response.success) {
                        sync.unsynced = false
                        db.itemDao().updateSync(sync)
                    }
                }

            } catch (e: Exception) {
                Log.e("PMR", e.message ?: "")
                return
            }
        }
    }
}