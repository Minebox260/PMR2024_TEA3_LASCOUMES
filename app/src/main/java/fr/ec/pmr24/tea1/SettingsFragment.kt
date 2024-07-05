package fr.ec.pmr24.tea1

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import fr.ec.pmr24.tea1.data.api.DataProvider

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val preferencesUser = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)

        val apiBaseUrl: Preference? = findPreference("base_url")
        apiBaseUrl?.summary = preferencesUser.getString("base_url", "http://tomnab.fr/todo-api/")

        apiBaseUrl?.setOnPreferenceChangeListener {
             preference, newValue ->
                val newURL = newValue as String

                if (newURL.isNotEmpty() && newURL.startsWith("http")) {
                    preference.summary = newURL

                    val editor = preferencesUser.edit()
                    editor.putString("base_url", newURL)
                    editor.apply()

                    DataProvider.init(requireContext())
                    true

                } else {
                    false
                }
            }
        }




}