package marvincz.cz.rssnotifier.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.Collections;

import marvincz.cz.rssnotifier.R;
import marvincz.cz.rssnotifier.adapter.ItemAdapter;
import marvincz.cz.rssnotifier.model.Rss;
import marvincz.cz.rssnotifier.retrofit.Client;
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
        fab.setOnClickListener(view -> Client.call("http://www.giantitp.com/comics/").rss().enqueue(new Callback<Rss>() {
            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {
                if (response != null && response.body() != null) {
                    adapter.replaceList(response.body().channel.items);
                }
            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                Log.e("MainActivity", "Error", t);
            }
        }));
    }

    private void goToLink(Uri link) {
        // TODO: 12.09.2018
    }
}
