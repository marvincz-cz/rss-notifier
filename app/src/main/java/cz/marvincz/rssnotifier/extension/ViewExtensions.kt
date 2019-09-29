package cz.marvincz.rssnotifier.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.graphics.drawable.DrawableCompat
import cz.marvincz.rssnotifier.extension.*

// Extensions for [View] class

/**
 * @see InputMethodManager.hideSoftInputFromWindow
 */
fun View.hideIme() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * @see ContextCompat.getColor
 */
@ColorInt
fun View.color(@ColorRes res: Int): Int = context.color(res)

/**
 * @see ContextCompat.getDrawable
 */
fun View.drawable(@DrawableRes res: Int): Drawable? = context.drawable(res)

/**
 * @see DrawableCompat.setTint
 */
fun View.tintedDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? =
    context.tintedDrawable(drawableId, colorId)

/**
 * @see Context.getString
 */
fun View.string(@StringRes res: Int): String = context.getString(res)

/**
 * @see Context.getString
 */
fun View.string(@StringRes res: Int, vararg formatArgs: Any): String =
    context.getString(res, formatArgs)

/**
 * Get color state list from resources
 *
 * @see ContextCompat.getColorStateList
 */
fun View.colors(@ColorRes stateListRes: Int): ColorStateList? = context.colors(stateListRes)

/**
 * @see Resources.getDimension
 */
fun View.dimension(@DimenRes res: Int): Float = resources.getDimension(res)

/**
 * @see Resources.getDimensionPixelSize
 */
fun View.dimensionSize(@DimenRes res: Int): Int = resources.getDimensionPixelSize(res)

@SuppressLint("Recycle")
fun View.resourceArray(@ArrayRes res: Int): IntArray = resources.obtainTypedArray(res)
    .use { array -> IntArray(array.length()) { array.getResourceId(it, 0) } }

/**
 * View artificial attribute that sets compound left drawable
 *
 * @see TextView.getCompoundDrawables
 * @see TextView.setCompoundDrawablesWithIntrinsicBounds
 */
var TextView.drawableLeft: Drawable?
    get() = compoundDrawables[0]
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(value, drawables[1], drawables[2], drawables[3])
    }

/**
 * View artificial attribute that sets compound right drawable
 *
 * @see TextView.getCompoundDrawables
 * @see TextView.setCompoundDrawablesWithIntrinsicBounds
 */
var TextView.drawableRight: Drawable?
    get() = compoundDrawables[2]
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], value, drawables[3])
    }

/**
 * View artificial attribute that sets compound top drawable
 *
 * @see TextView.getCompoundDrawables
 * @see TextView.setCompoundDrawablesWithIntrinsicBounds
 */
var TextView.drawableTop: Drawable?
    get() = compoundDrawables[1]
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], value, drawables[2], drawables[3])
    }

/**
 * View artificial attribute that sets compound bottom drawable
 *
 * @see TextView.getCompoundDrawables
 * @see TextView.setCompoundDrawablesWithIntrinsicBounds
 */
var TextView.drawableBottom: Drawable?
    get() = compoundDrawables[3]
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], value)
    }

/**
 * Inflate [layout] into this ViewGroup
 *
 * @see LayoutInflater.inflate
 */
fun ViewGroup.inflate(@LayoutRes layout: Int, attachToParent: Boolean = true): View =
    context.inflate(layout, this, attachToParent)