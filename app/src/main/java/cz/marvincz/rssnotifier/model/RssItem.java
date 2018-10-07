package cz.marvincz.rssnotifier.model;

import android.net.Uri;

public class RssItem {
    public String title;
    public String description;
    public Uri link;
    public transient boolean seen;

    /**
     * Based on {@link String#toString}
     * @return id based on link hash
     */
    public int getId() {
        String s = link.toString();
        int hash = 0;
        final int len = s.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                hash = 31 * hash + s.charAt(i);
            }
        }
        return hash;
    }
}
