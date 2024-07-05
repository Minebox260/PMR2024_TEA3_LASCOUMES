package fr.ec.pmr24.tea1

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import fr.ec.pmr24.tea1.data.sql.AppDatabase
import fr.ec.pmr24.tea1.recyclerviews.ListAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChoixListActivity : GenericActivity() {

    private val adapter = ListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_list)
        setupInsets()

        val hash = getSharedPreferences("user", MODE_PRIVATE).getString("hash" , "") ?: ""

        if (hash.isEmpty()) {
            val mainIntent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity((mainIntent))
            finish()
        }

        val recyclerview = findViewById<RecyclerView>(R.id.choose_list_recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        updateLists(hash)

        val newListInputText: TextInputEditText = findViewById(R.id.new_list_input_text)
        val newListSubmit: Button = findViewById(R.id.new_list_submit)

        if (!isNetworkAvailable()) {
            newListSubmit.isEnabled = false
            newListInputText.isEnabled = false
        }

        newListSubmit.setOnClickListener {
            val label = newListInputText.text.toString()
            if (label.isNotBlank()) {
                createList(hash, label)
            } else {
                showPopup(getString(R.string.new_list_empty))
            }
        }
    }

    private fun updateLists(hash: String) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@ChoixListActivity)
            if (isNetworkAvailable()) {
                try {
                    if (DataProvider.isInitialized()) {
                        val response = DataProvider.getLists(hash)
                        if (response.success) {
                            response.lists?.forEach { list ->
                                if (db.itemDao().getList(list.id) != null) {
                                    db.itemDao().updateList(list)
                                } else {
                                    db.itemDao().insertList(list)
                                }
                            }
                            adapter.show(response.lists ?: listOf())
                        } else {
                            showPopup(getString(R.string.lists_load_fail))
                        }
                    }
                } catch (e: HttpException) {
                    if (e.code() == 403) {
                        showPopup(getString(R.string.auth_failed))
                    } else
                        showPopup(getString(R.string.lists_load_fail))
                } catch (e: Exception) {
                    Log.e("PMR", e.message ?: "")
                }
            } else {
                val lists = db.itemDao().getLists()
                adapter.show(lists)
            }
        }
    }

    private fun createList(hash: String, label: String) {
        lifecycleScope.launch {
            if (isNetworkAvailable()) {
                try {
                    if (DataProvider.isInitialized()) {
                        val response = DataProvider.postList(hash, label)
                        if (response.success) {
                            Toast.makeText(
                                this@ChoixListActivity,
                                getString(R.string.list_post_successful), Toast.LENGTH_SHORT
                            ).show()
                            updateLists(hash)
                        } else {
                            showPopup(getString(R.string.list_post_failed))
                        }
                    }

                } catch (e: HttpException) {
                    if (e.code() == 403) {
                        showPopup(getString(R.string.auth_failed))
                    } else
                        showPopup(getString(R.string.list_post_failed))
                } catch (e: Exception) {
                    Log.e("PMR", e.message ?: "")
                }
            }
        }
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.choose_list)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}