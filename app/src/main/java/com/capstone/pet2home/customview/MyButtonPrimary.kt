package com.capstone.pet2home.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.graphics.drawable.DrawableCompat
import com.capstone.pet2home.R
import com.google.android.material.button.MaterialButton

class MyButtonPrimary: MaterialButton {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var buttonDrawable: Drawable = background
        buttonDrawable = DrawableCompat.wrap(buttonDrawable)
        background = if (isSelected){
            DrawableCompat.setTint(buttonDrawable, resources.getColor(R.color.teal_200))
            buttonDrawable
        }else{
            DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT)
            buttonDrawable
        }
        contentDescription = ""
    }
}