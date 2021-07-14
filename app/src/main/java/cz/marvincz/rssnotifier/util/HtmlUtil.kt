package cz.marvincz.rssnotifier.util

import android.text.Html


fun stripHtml(html: String?): String? {
    if (html == null) {
        return null
    }
    val text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT).toString()
    return text.replace(OBJECT_PLACEHOLDER_CHARACTER, "")
        .trim { it <= ' ' }
        .replace("\n\n", "\n")
}

private const val OBJECT_PLACEHOLDER_CHARACTER = "￼"