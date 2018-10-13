package cz.marvincz.rssnotifier.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.marvincz.rssnotifier.R;
import cz.marvincz.rssnotifier.RssApplication;
import cz.marvincz.rssnotifier.adapter.ItemAdapter;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.rssnotifier.repository.DataCallback;
import cz.marvincz.rssnotifier.repository.Repository;

public class MainActivity extends AppCompatActivity {
    // http://www.darthsanddroids.net/rss2.xml
    // http://www.giantitp.com/comics/oots.rss
    // https://www.erfworld.com/rss

    @Inject
    Repository repository;
    private ItemAdapter adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RssApplication.getAppComponent().inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.list);
        adapter = new ItemAdapter(this, Collections.emptyList(), this::goToLink);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> loadData());
    }

    private void loadData() {
        repository.getChannels(new DataCallback<List<RssChannel>>() {
            @Override
            public void onData(List<RssChannel> data) {
                List<RssItem> list = data.stream().flatMap(rss -> rss.items.stream())
                        .collect(Collectors.toList());
                adapter.replaceList(list);
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

    private void goToLink(Uri link) {
        startActivity(new Intent(Intent.ACTION_VIEW, link)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
