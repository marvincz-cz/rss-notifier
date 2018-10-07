package cz.marvincz.rssnotifier.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.marvincz.rssnotifier.R;
import cz.marvincz.rssnotifier.model.RssItem;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String OBJECT_PLACEHOLDER_CHARACTER = "￼";

    private final Context context;
    private List<RssItem> items;
    @NonNull
    private Callback callback;

    public ItemAdapter(Context context, List<RssItem> items, @NonNull Callback callback) {
        this.context = context;
        this.items = items;
        this.callback = callback;
    }

    public void setCallback(@NonNull Callback callback) {
        this.callback = callback;
    }

    public void replaceList(List<RssItem> list) {
        items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.item_two_line, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RssItem item = items.get(i);

        viewHolder.title.setText(stripHtml(item.title));
        viewHolder.description.setText(stripHtml(item.description));
        if (item.link != null) {
            viewHolder.actionIcon.setImageResource(R.drawable.ic_link);
            viewHolder.actionIcon.setVisibility(View.VISIBLE);
            viewHolder.itemView.setOnClickListener(v -> callback.goTo(item.link));
        } else {
            viewHolder.actionIcon.setImageResource(0);
            viewHolder.actionIcon.setVisibility(View.GONE);
            viewHolder.itemView.setOnClickListener(null);
        }
    }

    private String stripHtml(String html) {
        if (html == null) {
            return null;
        }
        String text;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            text = Html.fromHtml(html).toString();
        }
        return text.replace(OBJECT_PLACEHOLDER_CHARACTER, "")
                .trim()
                .replace("\n\n", "\n");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private ImageView icon;
        private ImageView actionIcon;

        ViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            icon = view.findViewById(R.id.icon);
            actionIcon = view.findViewById(R.id.actionIcon);
        }
    }

    public interface Callback {
        void goTo(Uri link);
    }
}
