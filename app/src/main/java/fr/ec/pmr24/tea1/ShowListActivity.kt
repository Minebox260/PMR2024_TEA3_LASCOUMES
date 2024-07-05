package fr.ec.pmr24.tea1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import fr.ec.pmr24.tea1.data.api.DataProvider
import fr.ec.pmr24.tea1.data.classes.ItemsSync
import fr.ec.pmr24.tea1.data.sql.AppDatabase
import fr.ec.pmr24.tea1.recyclerviews.ItemAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ShowListActivity : GenericActivity() {

    private lateinit var adapter : ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_list)
        setupInsets()

        val listId = intent.extras?.getString("list_id", "") ?: ""

        val hash = getSharedPreferences("user", MODE_PRIVATE).getString("hash" , "") ?: ""
        if (hash.isEmpty()) {
            val mainIntent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity((mainIntent))
            finish()
        }

        adapter = ItemAdapter {itemId, isChecked ->
            putItem(hash, listId, itemId, isChecked)
        }

        val recyclerview = findViewById<RecyclerView>(R.id.show_list_recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        updateItems(hash, listId)

        val newItemInputText: TextInputEditText = findViewById(R.id.new_item_input_text)
        val newItemURLText: TextInputEditText = findViewById(R.id.new_item_url_text)
        val newItemSubmit: Button = findViewById(R.id.new_item_submit)

        if (!isNetworkAvailable()) {
            newItemSubmit.isEnabled = false
            newItemInputText.isEnabled = false
            newItemURLText.isEnabled = false
        }
        newItemSubmit.setOnClickListener {
            val label = newItemInputText.text.toString()
            val url = newItemURLText.text.toString()
            if (label.isNotBlank() || !(url.isNotEmpty() && Patterns.WEB_URL.matcher(url).matches())) {
                createItem(hash, listId, label, url)
            } else {
                showPopup(getString(R.string.new_item_invalid))
            }
        }

    }

    private fun createItem(hash: String, listId: String, label: String, url: String? = null) {
        lifecycleScope.launch {
            if (isNetworkAvailable()) {
                try {
                    if (DataProvider.isInitialized()) {
                        val response = DataProvider.postItem(hash, listId, label, url)
                        if (response.success) {
                            Toast.makeText(
                                this@ShowListActivity,
                                getString(R.string.items_post_successful), Toast.LENGTH_SHORT
                            ).show()
                            updateItems(hash, listId)
                        } else {
                            showPopup(getString(R.string.item_post_failed))
                        }
                    }

                } catch (e: HttpException) {
                    if (e.code() == 403) {
                        showPopup(getString(R.string.auth_failed))
                    } else
                        showPopup(getString(R.string.item_post_failed))
                } catch (e: Exception) {
                    Log.e("PMR", e.message ?: "")
                }
            }
        }
    }

    private fun putItem(hash: String, listId: String, itemId: String, checked: Boolean) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@ShowListActivity)
            if (isNetworkAvailable()) {
                try {
                    if (DataProvider.isInitialized()) {
                        val response = DataProvider.putItem(hash, listId, itemId, if (checked) 1 else 0)
                        if (response.success && response.item != null) {
                            db.itemDao().updateItem(response.item)
                        } else {
                            showPopup(getString(R.string.item_put_failed))
                        }
                    }
                } catch (e: HttpException) {
                    if (e.code() == 403) {
                        showPopup(getString(R.string.auth_failed))
                    } else
                        showPopup(getString(R.string.item_put_failed))
                } catch (e: Exception) {
                    Log.e("PMR", e.message ?: "")
                }
            } else {
                val item = db.itemDao().getItem(itemId)
                if (item != null ) {
                    item.checked = if (checked) 1 else 0
                    db.itemDao().updateItem(item)
                    val sync = db.itemDao().getSync(item.id)
                    if (sync != null) {
                        sync.unsynced = true
                        db.itemDao().updateSync(sync)
                    }
                }

            }
        }
    }

    private fun updateItems(hash: String, listId: String) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@ShowListActivity)
            if (isNetworkAvailable()) {
                try {
                    if (DataProvider.isInitialized()) {
                        val response = DataProvider.getItems(hash, listId)
                        if (response.success) {
                            response.items?.forEach { item ->
                                if (db.itemDao().getItem(item.id) != null) {
                                    db.itemDao().updateItem(item)
                                } else {
                                    db.itemDao().insertItem(item)
                                }
                                if (db.itemDao().getSync(item.id) == null) {
                                    val sync = ItemsSync(itemId = item.id, listId = listId)
                                    db.itemDao().insertSync(sync)
                                }
                            }
                            adapter.show(response.items ?: listOf())
                        } else {
                            showPopup(getString(R.string.items_load_failed))
                        }
                    }
                } catch (e: HttpException) {
                    if (e.code() == 403) {
                        showPopup(getString(R.string.auth_failed))
                    } else
                        showPopup(getString(R.string.items_load_failed))
                } catch (e: Exception) {
                    Log.e("PMR", e.message ?: "")
                }
            } else {
                val items = db.itemDao().getListItems(listId)
                adapter.show(items)
            }

        }
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.show_list)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}