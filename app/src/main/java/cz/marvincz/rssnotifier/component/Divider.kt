package com.zebraapp.android.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.view.updateLayoutParams
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.extension.color
import cz.marvincz.rssnotifier.extension.dimensionSize

class Divider @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) :
    View(context, attributeSet) {

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    private val orientation: Int
    private val isHorizontal
        get() = orientation == HORIZONTAL

    private val inverseColor: Boolean

    private val thickness = dimensionSize(R.dimen.divider_thickness)

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.Divider)
        orientation = attributes.getInt(R.styleable.Divider_orientation, HORIZONTAL)
        inverseColor = attributes.getBoolean(R.styleable.Divider_inverseColor, false)
        attributes.recycle()
    }

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = if (inverseColor)
            color(R.color.divider_on_primary)
        else
            color(R.color.divider_on_surface)
    }

    // force thickness
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        updateLayoutParams {
            if (isHorizontal)
                height = thickness
            else
                width = thickness
        }
    }

    // declare thickness
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isHorizontal)
            setMeasuredDimension(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(thickness, MeasureSpec.EXACTLY)
            )
        else
            setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(thickness, MeasureSpec.EXACTLY),
                heightMeasureSpec
            )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isHorizontal)
            canvas.drawRect(
                paddingLeft.toFloat(),
                0f,
                (width - paddingRight).toFloat(),
                height.toFloat(),
                paint
            )
        else
            canvas.drawRect(
                0f,
                paddingTop.toFloat(),
                width.toFloat(),
                (height - paddingBottom).toFloat(),
                paint
            )
    }
}