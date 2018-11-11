package cz.marvincz.rssnotifier.activity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import cz.marvincz.rssnotifier.R;
import cz.marvincz.rssnotifier.RssApplication;
import cz.marvincz.rssnotifier.adapter.ChannelAdapter;
import cz.marvincz.rssnotifier.fragment.RssItemFragment;
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
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                adapter.reloadCurrentFragment();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {/*TODO add Channel?*/});

        pullDown = findViewById(R.id.pullDown);
        pullDown.setOnRefreshListener(() -> repository.download(new Callback()));

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentActivityCreated(@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment fragment,
                                                  @Nullable Bundle savedInstanceState) {
                if (fragment instanceof RssItemFragment) {
                    ((RssItemFragment) fragment).setSupplier(adapter::getRssItems);
                }
            }
        }, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        repository.loadData(new Callback());
    }

    private class Callback implements DataCallback<List<ChannelWithItems>> {
        @Override
        public void onData(@NonNull List<ChannelWithItems> data, boolean finalResult) {
            if (finalResult) {
                pullDown.setRefreshing(false);
            }
            adapter.replaceList(data);
        }

        @Override
        public void onLoading() {
            if (!pullDown.isRefreshing()) {
                // TODO: 10.11.2018
            }
        }

        @Override
        public void onError() {
            pullDown.setRefreshing(false);
            Snackbar snackbar = Snackbar.make(fab, "Error", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Close", v -> snackbar.dismiss());
            snackbar.show();
        }
    }
}
