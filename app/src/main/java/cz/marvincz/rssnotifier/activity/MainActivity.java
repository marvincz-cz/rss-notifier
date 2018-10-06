package cz.marvincz.rssnotifier.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.Collections;

import cz.marvincz.rssnotifier.R;
import cz.marvincz.rssnotifier.adapter.ItemAdapter;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.retrofit.Client;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.list);
        ItemAdapter adapter = new ItemAdapter(this, Collections.emptyList(), this::goToLink);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Client.call().rss("http://www.darthsanddroids.net/rss2.xml").enqueue(new Callback<RssChannel>() {
            @Override
            public void onResponse(@NonNull Call<RssChannel> call, @NonNull Response<RssChannel> response) {
                if (response.body() != null) {
                    adapter.replaceList(response.body().items);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RssChannel> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Error", t);
            }
        }));
    }

    private void goToLink(Uri link) {
        // TODO: 12.09.2018
    }
}
