package cz.marvincz.rssnotifier.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.marvincz.rssnotifier.background.WorkScheduler
import cz.marvincz.rssnotifier.composable.ChannelsScreen
import cz.marvincz.rssnotifier.composable.ManageChannelsScreen
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "channels") {
                composable("channels") { ChannelsScreen(navController) }
                composable("manageChannels") { ManageChannelsScreen(navController) }
            }
        }

        val workScheduler: WorkScheduler = getKoin().get()
        workScheduler.scheduleWork()
    }
}
