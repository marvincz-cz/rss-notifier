package cz.marvincz.rssnotifier.repository;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

@UiThread
public interface DataCallback<T> {
    void onData(@NonNull T data);
    void onLoading();
    void onError();
}
