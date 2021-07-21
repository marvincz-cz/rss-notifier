package cz.marvincz.rssnotifier.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import cz.marvincz.rssnotifier.background.WorkScheduler
import cz.marvincz.rssnotifier.composable.ChannelsScreen
import cz.marvincz.rssnotifier.composable.ManageChannelsScreen
import cz.marvincz.rssnotifier.composable.SettingsScreen
import cz.marvincz.rssnotifier.navigation.NavigationScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = NavigationScreen.CHANNELS.route) {
                composable(NavigationScreen.CHANNELS.route) {
                    ChannelsScreen(navController)
                }
                composable(
                    NavigationScreen.MANAGE_CHANNELS.route,
                    arguments = listOf(navArgument(NavigationScreen.MANAGE_CHANNELS.param!!) {
                        type = NavType.BoolType
                        defaultValue = false
                    })
                ) { backStackEntry ->
                    ManageChannelsScreen(navController, backStackEntry.arguments?.getBoolean(NavigationScreen.MANAGE_CHANNELS.param))
                }
                composable(NavigationScreen.SETTINGS.route) {
                    SettingsScreen(navController)
                }
            }
        }

        lifecycleScope.launch {
            scheduleWork()
        }
    }

    private suspend fun scheduleWork() {
        val workScheduler: WorkScheduler = getKoin().get()
        workScheduler.scheduleWork()
    }
}
