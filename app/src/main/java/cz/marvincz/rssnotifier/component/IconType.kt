package cz.marvincz.rssnotifier.component

import androidx.annotation.DimenRes
import cz.marvincz.rssnotifier.R

enum class IconType(val index: Int, @DimenRes val size: Int) {
    NONE(0, R.dimen.dimen_0),
    SMALL(1, R.dimen.icon_small),
    MEDIUM(2, R.dimen.icon_medium),
    LARGE(3, R.dimen.icon_large);

    companion object {
        operator fun get(index: Int) = values().single { it.index == index }
    }
}