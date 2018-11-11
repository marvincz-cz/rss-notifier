package cz.marvincz.rssnotifier.adapter;

import android.view.ViewGroup;

import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import cz.marvincz.rssnotifier.fragment.RssItemFragment;
import cz.marvincz.rssnotifier.model.ChannelWithItems;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;

public class ChannelAdapter extends FragmentStatePagerAdapter {
    @NonNull
    private List<ChannelWithItems> channels;

    @Nullable
    private RssItemFragment currentFragment;

    public ChannelAdapter(@NonNull FragmentManager fragmentManager, @NonNull List<ChannelWithItems> channels) {
        super(fragmentManager);
        this.channels = channels;
    }

    @Override
    public Fragment getItem(int position) {
        return RssItemFragment.newInstance(channels.get(position));
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    public void replaceList(@NonNull List<ChannelWithItems> list) {
        this.channels = list;
        notifyDataSetChanged();

        reloadCurrentFragment();
    }

    public void reloadCurrentFragment() {
        if (currentFragment != null) {
            currentFragment.reloadData();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return channels.get(position).title;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        currentFragment = (RssItemFragment) object;

        reloadCurrentFragment();
    }

    public List<RssItem> getRssItems(final RssChannel rssChannel) {
        return channels.stream()
                .filter(ch -> ch.accessUrl.equals(rssChannel.accessUrl))
                .flatMap(ch -> ch.items.stream())
                .collect(Collectors.toList());
    }
}
