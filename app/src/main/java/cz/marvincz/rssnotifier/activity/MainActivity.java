package cz.marvincz.rssnotifier.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.marvincz.rssnotifier.R;
import cz.marvincz.rssnotifier.adapter.ItemAdapter;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.retrofit.Client;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    // http://www.darthsanddroids.net/rss2.xml
    // http://www.giantitp.com/comics/oots.rss
    // https://www.erfworld.com/rss

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
        startActivity(new Intent(Intent.ACTION_VIEW, link)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
