package cz.marvincz.rssnotifier.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.RssApplication
import cz.marvincz.rssnotifier.adapter.ChannelAdapter
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.room.Database
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    // http://www.darthsanddroids.net/rss2.xml
    // http://www.giantitp.com/comics/oots.rss
    // https://www.erfworld.com/rss

    @Inject
    internal lateinit var repository: Repository
    @Inject
    internal lateinit var database: Database

    private lateinit var adapter: ChannelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RssApplication.appComponent.inject(this)
        setSupportActionBar(toolbar)

        adapter = ChannelAdapter(this, supportFragmentManager)
        database.dao().getChannels().observe(this, Observer { adapter.data = it })
        pager.adapter = adapter

        fab.setOnClickListener { /*TODO add Channel?*/}

        pullDown.setOnRefreshListener { repository.download(true) { pullDown.isRefreshing = false } }
    }

    override fun onResume() {
        super.onResume()
        repository.download()
    }
}
