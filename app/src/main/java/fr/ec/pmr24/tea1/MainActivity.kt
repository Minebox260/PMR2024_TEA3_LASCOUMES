package fr.ec.pmr24.tea1

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import fr.ec.pmr24.tea1.data.api.DataProvider
import kotlinx.coroutines.launch
import retrofit2.HttpException


class MainActivity : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setupInsets()

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(NetworkChangeReceiver(), intentFilter)

        DataProvider.init(this)

        val usernameInput: EditText = findViewById(R.id.username_input_text)
        val passwordInput: EditText = findViewById(R.id.password_input_text)
        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.isEnabled = false

        val hash = getSharedPreferences("user", Context.MODE_PRIVATE).getString("hash", "") ?: ""

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            if (username.isNotBlank() && password.isNotBlank()) {
                login(username, password)

            } else {
                showPopup(getString(R.string.username_empty))
            }
        }

        lifecycleScope.launch{
            val isNetworkAvailable = isNetworkAvailable()
            loginButton.isEnabled = isNetworkAvailable
            if (!isNetworkAvailable) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.login_no_network), Toast.LENGTH_SHORT
                ).show()
            }
            if (hash.isNotEmpty()) {
                val choixListIntent = Intent(
                    this@MainActivity,
                    ChoixListActivity::class.java
                )
                startActivity((choixListIntent))
            }
        }
    }


    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun login(username: String, password: String) {
        lifecycleScope.launch {
            try {
                if (DataProvider.isInitialized()) {
                    val response = DataProvider.authenticate(username, password)

                    if (response.success) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.login_successful), Toast.LENGTH_SHORT
                        ).show()
                        with(getSharedPreferences("user", Context.MODE_PRIVATE).edit()) {
                            putString("hash", response.hash)
                            apply()
                        }
                        val choixListIntent = Intent(
                            this@MainActivity,
                            ChoixListActivity::class.java
                        )
                        this@MainActivity.startActivity(choixListIntent)

                    } else {
                        showPopup(getString(R.string.login_failed))
                    }
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    showPopup(getString(R.string.login_failed_credentials))
                } else
                    showPopup(getString(R.string.login_failed))
            } catch (e: Exception) {
                Log.e("PMR", e.message ?: "")
            }
        }
    }


}
