package cz.marvincz.rssnotifier.extension

import androidx.fragment.app.DialogFragment

fun DialogFragment.setLayout(width: Int, height: Int) {
    dialog?.window?.setLayout(width, height)
}