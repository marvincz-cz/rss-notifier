package cz.marvincz.rssnotifier.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import cz.marvincz.rssnotifier.R;
import cz.marvincz.rssnotifier.RssApplication;
import cz.marvincz.rssnotifier.adapter.ChannelAdapter;
import cz.marvincz.rssnotifier.model.ChannelWithItems;
import cz.marvincz.rssnotifier.repository.DataCallback;
import cz.marvincz.rssnotifier.repository.Repository;

public class MainActivity extends AppCompatActivity {
    // http://www.darthsanddroids.net/rss2.xml
    // http://www.giantitp.com/comics/oots.rss
    // https://www.erfworld.com/rss

    @Inject
    Repository repository;

    private SwipeRefreshLayout pullDown;
    private FloatingActionButton fab;
    private ChannelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RssApplication.getAppComponent().inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.pager);
        adapter = new ChannelAdapter(getSupportFragmentManager(), Collections.emptyList());
        viewPager.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {/*TODO*/});

        pullDown = findViewById(R.id.pullDown);
        pullDown.setOnRefreshListener(() -> this.reloadData());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void reloadData() {
        repository.reload(new DataCallback<List<ChannelWithItems>>() {
            @Override
            public void onData(@NonNull List<ChannelWithItems> data) {
                pullDown.setRefreshing(false);
                adapter.replaceList(data);
            }

            @Override
            public void onLoading() {
                /* no-op */
            }

            @Override
            public void onError() {
                pullDown.setRefreshing(false);
                Snackbar snackbar = Snackbar.make(fab, "Error", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Close", v -> snackbar.dismiss());
                snackbar.show();
            }
        });
    }

    private void loadData() {
        repository.getChannels(new DataCallback<List<ChannelWithItems>>() {
            @Override
            public void onData(List<ChannelWithItems> data) {
                adapter.replaceList(data);
            }

            @Override
            public void onLoading() {
                Toast.makeText(MainActivity.this, "loading...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Snackbar snackbar = Snackbar.make(fab, "Error", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Close", v -> snackbar.dismiss());
                snackbar.show();
            }
        });
    }
}
