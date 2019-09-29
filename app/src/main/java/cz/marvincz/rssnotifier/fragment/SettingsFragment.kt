package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val wifiPreference = SwitchPreferenceCompat(context).apply {
            setDefaultValue(false)
            key = context.getString(R.string.preference_wifi_only)
            setTitle(R.string.preference_wifi_only_title)
            setSummary(R.string.preference_wifi_only_summary)
            setIcon(R.drawable.ic_wifi)

            setOnPreferenceChangeListener { _, _ ->
                viewModel.rescheduleWork()
                true
            }
        }
        screen.addPreference(wifiPreference)

        preferenceScreen = screen
    }
}