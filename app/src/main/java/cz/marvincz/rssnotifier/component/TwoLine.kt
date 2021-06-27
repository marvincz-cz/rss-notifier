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
import cz.marvincz.rssnotifier.databinding.TwoLineInternalBinding
import cz.marvincz.rssnotifier.extension.*

class TwoLine @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) :
        ConstraintLayout(context, attributeSet) {

    private val binding = TwoLineInternalBinding.bind(inflate(R.layout.two_line_internal))

    private var iconType = IconType.SMALL
        set(value) {
            field = value
            adjustSize()
        }

    private val heights = resourceArray(R.array.two_line_heights)
    private val iconTextMargins = resourceArray(R.array.icon_text_margins)
    private val iconMargins = resourceArray(R.array.two_line_icon_margins)

    private var layoutHeight = 0

    init {
        background = drawable(R.drawable.ripple)

        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.TwoLine)
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
        iconType = IconType[attributes.getInt(R.styleable.TwoLine_iconType, 1)]

        attributes.getDrawable(R.styleable.TwoLine_drawable)
                ?.let { drawable = it }

        attributes.getText(R.styleable.TwoLine_text)
                ?.let { text = it }

        attributes.getText(R.styleable.TwoLine_secondaryText)
                ?.let { secondaryText = it }

        attributes.getDrawable(R.styleable.TwoLine_actionDrawable)
                ?.let { actionDrawable = it }

        emphasis = attributes.getBoolean(R.styleable.TwoLine_emphasis, false)
    }

    private fun adjustSize() {
        val effectiveIconType = if (drawable != null) iconType else IconType.NONE
        val index = effectiveIconType.index

        layoutHeight = dimensionSize(heights[index])
        val iconSize = dimensionSize(effectiveIconType.size)
        val iconTextMargin = dimensionSize(iconTextMargins[index])
        val iconMargin = dimensionSize(iconMargins[index])

        binding.itemIcon.updateLayoutParams<MarginLayoutParams> {
            width = iconSize
            height = iconSize
            topMargin = iconMargin
            bottomMargin = iconMargin
        }
        binding.itemTitle.updateLayoutParams<MarginLayoutParams> {
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
        get() = binding.itemTitle.text
        set(value) {
            binding.itemTitle.text = value
        }

    fun setText(@StringRes resId: Int) {
        binding.itemTitle.setText(resId)
    }

    var secondaryText: CharSequence?
        get() = binding.itemSecondary.text
        set(value) {
            binding.itemSecondary.text = value
        }

    fun setSecondaryText(@StringRes resId: Int) {
        binding.itemSecondary.setText(resId)
    }

    var drawable: Drawable?
        get() = binding.itemIcon.drawable
        set(value) {
            binding.itemIcon.setImageDrawable(value)
            updateIconVisibility()
        }

    fun setDrawable(@DrawableRes resId: Int) {
        binding.itemIcon.setImageResource(resId)
        updateIconVisibility()
    }

    private fun updateIconVisibility() {
        binding.itemIcon.isVisible = binding.itemIcon.drawable != null
        adjustSize()
    }

    var actionDrawable: Drawable?
        get() = binding.itemAction.drawable
        set(value) {
            binding.itemAction.setImageDrawable(value)
            updateActionVisibility()
        }

    fun setActionDrawable(@DrawableRes resId: Int) {
        binding.itemAction.setImageResource(resId)
        updateActionVisibility()
    }

    var actionDescription: CharSequence
        get() = binding.itemAction.contentDescription
        set(value) {
            binding.itemAction.contentDescription = value
        }

    fun setActionDescription(@StringRes resId: Int) {
        actionDescription = string(resId)
    }

    private fun updateActionVisibility() {
        binding.itemAction.isVisible = binding.itemAction.drawable != null
    }

    fun setActionListener(listener: (() -> Unit)?) {
        binding.itemAction.setOnClickListener(listener?.let { OnClickListener { it() } })
        binding.itemAction.background = if (listener != null)
            drawable(R.drawable.ripple_icon)
        else null
    }

    var emphasis: Boolean = false
        set(value) {
            field = value
            if (value) {
                binding.itemTitle.setTextAppearance(R.style.Text_Normal_Emphasis)
                binding.itemSecondary.setTextAppearance(R.style.Text_Secondary_Emphasis)
            } else {
                binding.itemTitle.setTextAppearance(R.style.Text_Normal)
                binding.itemSecondary.setTextAppearance(R.style.Text_Secondary)
            }
        }
}