package com.espoir.autoxmlib.drawable

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

object TextViewFactory {
    @JvmStatic
    fun setTextGradientColor(context: Context, attrs: AttributeSet, textView: TextView) {
        TextViewGradientColor().invoke(context, attrs, textView)
    }
}