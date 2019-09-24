package cz.marvincz.rssnotifier.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.marvincz.rssnotifier.adapter.ChannelAdapter
import cz.marvincz.rssnotifier.repository.Repository
import org.koin.android.ext.android.inject

class ChannelsActivity : AppCompatActivity() {
    // http://www.darthsanddroids.net/rss2.xml
    // http://www.giantitp.com/comics/oots.rss
    // https://www.erfworld.com/rss

    private val repository: Repository by inject()

    private lateinit var adapter: ChannelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_channels)
//        setSupportActionBar(toolbar)
//
//        adapter = ChannelAdapter(supportFragmentManager)
//        repository.getChannels().observe(this, Observer { adapter.data = it })
//        pager.adapter = adapter
//
//        fab.setOnClickListener { urlDialog() }
//
//        pullDown.setOnRefreshListener { repository.download(true) { pullDown.isRefreshing = false } }
    }

    override fun onResume() {
        super.onResume()
        repository.download()
    }

    private fun urlDialog() {
//        val margin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_padding)
//        val urlInput = TextInputEditText(this)
//        AlertDialog.Builder(this)
//                .setTitle(R.string.dialog_add_channel_title)
//                .setMessage(R.string.dialog_add_channel_message)
//                .setPositiveButton(R.string.action_ok) { _, _ -> repository.addChannel(urlInput.text.toString()) }
//                .setView(urlInput)
//                .create()
//                .show()
//        (urlInput.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
//            marginStart = margin
//            marginEnd = margin
//        }
//        urlInput.requestLayout()
    }
}
