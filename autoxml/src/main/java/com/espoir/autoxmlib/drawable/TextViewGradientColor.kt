package com.espoir.autoxmlib.drawable

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.TextView
import com.espoir.autoxmlib.R

class TextViewGradientColor : ITextViewOperator {
    private var endColor = -1
    private var startColor = -1
    private var orientation = 0
    override fun invoke(context: Context, attrs: AttributeSet, textView: TextView) {
        val textTa = context.obtainStyledAttributes(attrs, R.styleable.shape_text)
        try {
            if (textTa.indexCount == 0) {
                return
            }
            for (i in 0 until textTa.indexCount) {
                when (val attr = textTa.getIndex(i)) {
                    R.styleable.shape_text_shape_text_gradient_endColor -> {
                        endColor = textTa.getColor(attr, -1)
                    }
                    R.styleable.shape_text_shape_text_gradient_startColor -> {
                        startColor = textTa.getColor(attr, -1)
                    }
                    R.styleable.shape_text_shape_text_gradient_orientation -> {
                        orientation = textTa.getInt(attr, 0)
                    }
                }
            }
            if (endColor == -1 && startColor != -1) {
                textView.setTextColor(startColor)
            } else if (startColor == -1 && endColor != -1) {
                textView.setTextColor(endColor)
            } else if (endColor != -1 && startColor != -1) {
                if (orientation == 0) {
                    textView.post {
                        textView.paint.shader = LinearGradient(
                            0f, 0f, 0f, textView.paint.descent() - textView.paint.ascent(),
                            startColor, endColor, Shader.TileMode.REPEAT
                        )
                        textView.invalidate()
                    }
                } else {
                    textView.post {
                        textView.paint.shader = LinearGradient(
                            0f,
                            0f,
                            textView.measuredWidth.toFloat(),
                            0f,
                            startColor,
                            endColor,
                            Shader.TileMode.REPEAT
                        )
                        textView.invalidate()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            textTa.recycle()
        }
    }
}