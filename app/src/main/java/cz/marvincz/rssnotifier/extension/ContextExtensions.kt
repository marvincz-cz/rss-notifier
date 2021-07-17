package cz.marvincz.rssnotifier.extension

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

// Extensions to context class

/**
 * @see ContextCompat.getColor
 */
@ColorInt
fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

/**
 * @see ContextCompat.getDrawable
 */
fun Context.drawable(@DrawableRes res: Int): Drawable? = ContextCompat.getDrawable(this, res)

/**
 * @see DrawableCompat.setTint
 */
fun Context.tintedDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? =
    drawable(drawableId)
        ?.also {
            it.mutate()
            DrawableCompat.setTint(it, color(colorId))
        }

/**
 * @see Resources.getQuantityString
 */
fun Context.quantityString(@PluralsRes res: Int, quantity: Int): CharSequence =
    resources.getQuantityString(res, quantity)

/**
 * @see Resources.getQuantityString
 */
fun Context.quantityString(@PluralsRes res: Int, quantity: Int, vararg formatArgs: Any): CharSequence =
    resources.getQuantityString(res, quantity, *formatArgs)

/**
 * @see ContextCompat.getColorStateList
 */
fun Context.colors(@ColorRes stateListRes: Int): ColorStateList? =
    ContextCompat.getColorStateList(this, stateListRes)

/**
 * Inflater layout defined by [res] into view group [parent]. Optionally attach view to parent with [attachView] attribute
 *
 * @see LayoutInflater.inflate
 */
fun Context.inflate(@LayoutRes res: Int, parent: ViewGroup, attachView: Boolean = true): View =
    LayoutInflater.from(this).inflate(res, parent, attachView)

fun Context.goToLink(link: String?) = goToLink(link?.let { Uri.parse(it) })

fun Context.goToLink(link: Uri?) {
    link?.let {
        startActivity(
            Intent(Intent.ACTION_VIEW, it)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}