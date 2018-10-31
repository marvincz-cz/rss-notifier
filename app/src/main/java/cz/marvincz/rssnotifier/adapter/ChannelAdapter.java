package cz.marvincz.rssnotifier.adapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import cz.marvincz.rssnotifier.fragment.ListFragment;
import cz.marvincz.rssnotifier.model.ChannelWithItems;

public class ChannelAdapter extends FragmentStatePagerAdapter {
    @NonNull
    private List<ChannelWithItems> channels;

    public ChannelAdapter(@NonNull FragmentManager fragmentManager, @NonNull List<ChannelWithItems> channels) {
        super(fragmentManager);
        this.channels = channels;
    }

    @Override
    public Fragment getItem(int position) {
        return ListFragment.newInstance(channels.get(position));
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    public void replaceList(@NonNull List<ChannelWithItems> list) {
        this.channels = list;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return channels.get(position).title;
    }
}
