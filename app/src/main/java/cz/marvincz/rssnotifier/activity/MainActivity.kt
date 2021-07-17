package cz.marvincz.rssnotifier.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.marvincz.rssnotifier.background.WorkScheduler
import cz.marvincz.rssnotifier.composable.ChannelsScreen
import cz.marvincz.rssnotifier.composable.ManageChannelsScreen
import cz.marvincz.rssnotifier.composable.SettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "channels") {
                composable("channels") { ChannelsScreen(navController) }
                composable("manageChannels") { ManageChannelsScreen(navController) }
                composable("settings") { SettingsScreen(navController) }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            scheduleWork()
        }
    }

    private suspend fun scheduleWork() {
        val workScheduler: WorkScheduler = getKoin().get()
        workScheduler.scheduleWork()
    }
}
