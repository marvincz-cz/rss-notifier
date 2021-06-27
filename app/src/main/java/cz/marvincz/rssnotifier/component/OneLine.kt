package cz.marvincz.rssnotifier.component

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.databinding.OneLineInternalBinding
import cz.marvincz.rssnotifier.extension.*

class OneLine @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) :
        ConstraintLayout(context, attributeSet) {

    private val binding = OneLineInternalBinding.bind(inflate(R.layout.one_line_internal))

    val icon get() = binding.itemIcon

    private var iconType = IconType.SMALL
        set(value) {
            field = value
            adjustSize()
        }

    private val heights = resourceArray(R.array.one_line_heights)
    private val iconTextMargins = resourceArray(R.array.icon_text_margins)

    private var layoutHeight = 0

    init {
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

        emphasis = attributes.getBoolean(R.styleable.OneLine_emphasis, false)
    }

    private fun adjustSize() {
        val effectiveIconType = if (drawable != null) iconType else IconType.NONE
        val index = effectiveIconType.index

        layoutHeight = dimensionSize(heights[index])
        val iconSize = dimensionSize(effectiveIconType.size)
        val iconTextMargin = dimensionSize(iconTextMargins[index])

        binding.itemIcon.updateLayoutParams {
            width = iconSize
            height = iconSize
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
            } else {
                binding.itemTitle.setTextAppearance(R.style.Text_Normal)
            }
        }
}