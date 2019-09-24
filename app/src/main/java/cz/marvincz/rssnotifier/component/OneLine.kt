package cz.marvincz.rssnotifier.component

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.extension.dimensionSize
import cz.marvincz.rssnotifier.extension.drawable
import cz.marvincz.rssnotifier.extension.inflate
import cz.marvincz.rssnotifier.extension.resourceArray
import kotlinx.android.synthetic.main.one_line_internal.view.*

class OneLine @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    private var iconType = IconType.SMALL
        set(value) {
            field = value
            adjustSize()
        }

    private val heights = resourceArray(R.array.one_line_heights)
    private val iconTextMargins = resourceArray(R.array.icon_text_margins)

    private var layoutHeight = 0

    init {
        inflate(R.layout.one_line_internal)
        background = drawable(R.drawable.ripple)

        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.OneLine)
        useAttributes(attributes)
        attributes.recycle()

        if (attributeSet == null) {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                layoutHeight
            )
        }
    }

    private fun useAttributes(attributes: TypedArray) {
        iconType = IconType[attributes.getInt(R.styleable.OneLine_iconType, 1)]

        attributes.getDrawable(R.styleable.OneLine_drawable)
            ?.let { drawable = it }

        attributes.getText(R.styleable.OneLine_text)
            ?.let { text = it }

        attributes.getDrawable(R.styleable.OneLine_actionDrawable)
            ?.let { actionDrawable = it }
    }

    private fun adjustSize() {
        val effectiveIconType = if (drawable != null) iconType else IconType.NONE
        val index = effectiveIconType.index

        layoutHeight = dimensionSize(heights[index])
        val iconSize = dimensionSize(effectiveIconType.size)
        val iconTextMargin = dimensionSize(iconTextMargins[index])

        item_icon.updateLayoutParams {
            width = iconSize
            height = iconSize
        }
        item_title.updateLayoutParams<MarginLayoutParams> {
            marginStart = iconTextMargin
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun isClickable() = hasOnClickListeners()

    var text: CharSequence?
        get() = item_title.text
        set(value) {
            item_title.text = value
        }

    fun setText(@StringRes resId: Int) {
        item_title.setText(resId)
    }

    var drawable: Drawable?
        get() = item_icon.drawable
        set(value) {
            item_icon.setImageDrawable(value)
            updateIconVisibility()
        }

    fun setDrawable(@DrawableRes resId: Int) {
        item_icon.setImageResource(resId)
        updateIconVisibility()
    }

    private fun updateIconVisibility() {
        item_icon.isVisible = item_icon.drawable != null
        adjustSize()
    }

    var actionDrawable: Drawable?
        get() = item_action.drawable
        set(value) {
            item_action.setImageDrawable(value)
            updateActionVisibility()
        }

    fun setActionDrawable(@DrawableRes resId: Int) {
        item_action.setImageResource(resId)
        updateActionVisibility()
    }

    private fun updateActionVisibility() {
        item_action.isVisible = item_action.drawable != null
    }

    fun setActionListener(listener: (() -> Unit)?) {
        item_action.setOnClickListener(listener?.let { OnClickListener { it() } })
        item_action.background = if (listener != null)
            drawable(R.drawable.ripple_icon)
        else null
    }
}