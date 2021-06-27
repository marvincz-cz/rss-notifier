package cz.marvincz.rssnotifier.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.background.WorkScheduler
import cz.marvincz.rssnotifier.databinding.ActivityMainBinding
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        val workScheduler: WorkScheduler = getKoin().get()
        workScheduler.scheduleWork()
    }
}
