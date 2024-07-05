package fr.ec.pmr24.tea1

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


open class GenericActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            this.startActivity(intent)
        } else if (id == R.id.action_logout) {
           logout()
        }
        return super.onOptionsItemSelected(item)
    }

    fun logout() {
        val editor = getSharedPreferences("user", MODE_PRIVATE).edit()
        editor.remove("hash")
        editor.apply()
        val intent = Intent(this@GenericActivity, MainActivity::class.java)
        this.startActivity(intent)
    }

    fun showPopup(message: String) {
        val builder = AlertDialog.Builder(this, R.style.DialogTheme)
        .setTitle(getString(R.string.dialog_title))
        .setMessage(message)
        .setPositiveButton(getString(R.string.dialog_dismiss)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        return caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}