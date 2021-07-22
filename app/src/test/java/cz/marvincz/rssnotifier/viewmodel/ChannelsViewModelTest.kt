package cz.marvincz.rssnotifier.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import com.jraska.livedata.TestObserver
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.repository.Repository
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime
import java.util.concurrent.Executors

@OptIn(ExperimentalCoroutinesApi::class)
class ChannelsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val repository = mockk<Repository>()
    private val dataStore = mockk<DataStore<Preferences>>(relaxed = true)
    private val channelsData = MutableLiveData<List<RssChannel>>()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        every { repository.getChannels() } returns channelsData
        coJustRun { repository.refreshAll(any()) }
        every { repository.getItems(URL) } returns MutableLiveData(ITEMS)
        every { repository.getItems(URL2) } returns MutableLiveData(ITEMS2)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.close()
    }

    @Test
    fun getItems() {
        val vm = ChannelsViewModel(repository, dataStore)

        TestObserver.test(vm.items)
            .assertNoValue()
            .also { channelsData.value = CHANNELS }
            // assuming vm.selectedChannelIndex initial value of 0
            .assertValue(ITEMS)
            .also { vm.onChannelSelected(1) }
            .assertValue(ITEMS2)
    }

    @Test
    fun channelRemoved() {
        val vm = ChannelsViewModel(repository, dataStore)
        channelsData.value = CHANNELS
        vm.onChannelSelected(1)

        TestObserver.test(vm.items)
            .assertValue(ITEMS2)
            .also { channelsData.value = CHANNELS.take(1) }
            .assertValue(ITEMS)

        TestObserver.test(vm.selectedChannelIndex)
            .assertValue(0)
    }
}

private const val URL = "URL"
private const val URL2 = "URL2"

private val CHANNELS = listOf(
    RssChannel(URL, null, "", null, 0, ZonedDateTime.now()),
    RssChannel(URL2, null, "", null, 0, ZonedDateTime.now()),
)

private val ITEMS = emptyList<RssItem>()
private val ITEMS2 = listOf(RssItem("", null, null, null, null))
