package com.espoir.autoxmlib.drawable

import android.content.res.TypedArray
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import com.espoir.autoxml.R
import org.xmlpull.v1.XmlPullParserException

class GradientDrawableCreator(private val typedArray: TypedArray, private val gradientState: Int = -1) :
    ICreateDrawable {
    override fun create(): Drawable {
        val drawable = GradientDrawable()
        val cornerRadius = FloatArray(8)
        var sizeWidth = 0f
        var sizeHeight = 0f
        var strokeWidth = -1f
        var strokeDashWidth = 0f
        var strokeColor = 0
        var solidColor = 0
        var strokeGap = 0f
        var centerX = 0f
        var centerY = 0f
        var centerColor = 0
        var startColor = 0
        var endColor = 0
        var gradientType = GradientDrawable.LINEAR_GRADIENT
        var gradientAngle = 0
        val padding = Rect()

        for (i in 0..typedArray.indexCount) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.autoxml_shape_type) {
                drawable.shape = typedArray.getInt(attr, 0)
            } else if (attr == R.styleable.autoxml_shape_solid_color) {
                solidColor = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.autoxml_shape_corner) {
                drawable.cornerRadius = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_corner_bottom_left) {
                cornerRadius[6] = typedArray.getDimension(attr, 0f)
                cornerRadius[7] = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_corner_bottom_right) {
                cornerRadius[4] = typedArray.getDimension(attr, 0f)
                cornerRadius[5] = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_corner_top_left) {
                cornerRadius[0] = typedArray.getDimension(attr, 0f)
                cornerRadius[1] = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_corner_top_right) {
                cornerRadius[2] = typedArray.getDimension(attr, 0f)
                cornerRadius[3] = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_gradient_angle) {
                if (gradientState == -1) {
                    gradientAngle = typedArray.getInteger(attr, 0)
                }
            } else if (attr == R.styleable.autoxml_shape_gradient_centerX) {
                if (gradientState == -1) {
                    centerX = typedArray.getFloat(attr, -1f)
                }
            } else if (attr == R.styleable.autoxml_shape_gradient_centerY) {
                if (gradientState == -1) {
                    centerY = typedArray.getFloat(attr, -1f)
                }
            } else if (attr == R.styleable.autoxml_shape_gradient_centerColor) {
                if (gradientState == -1) {
                    centerColor = typedArray.getColor(attr, 0)
                }
            } else if (attr == R.styleable.autoxml_shape_gradient_endColor) {
                if (gradientState == -1) {
                    endColor = typedArray.getColor(attr, 0)
                }
            } else if (attr == R.styleable.autoxml_shape_gradient_startColor) {
                if (gradientState == -1) {
                    startColor = typedArray.getColor(attr, 0)
                }
            } else if (attr == R.styleable.autoxml_shape_gradient_gradientRadius) {
                if (gradientState == -1) {
                    drawable.gradientRadius = typedArray.getDimension(attr, 0f)
                }
            } else if (attr == R.styleable.autoxml_shape_gradient_type) {
                if (gradientState == -1) {
                    gradientType = typedArray.getInt(attr, 0)
                }
                drawable.gradientType = gradientType
            } else if (attr == R.styleable.autoxml_shape_gradient_useLevel) {
                if (gradientState == -1) {
                    drawable.useLevel = typedArray.getBoolean(attr, false)
                }
            } else if (attr == R.styleable.autoxml_shape_padding_left) {
                padding.left = typedArray.getDimension(attr, 0f).toInt()
            } else if (attr == R.styleable.autoxml_shape_padding_top) {
                padding.top = typedArray.getDimension(attr, 0f).toInt()
            } else if (attr == R.styleable.autoxml_shape_padding_right) {
                padding.right = typedArray.getDimension(attr, 0f).toInt()
            } else if (attr == R.styleable.autoxml_shape_padding_bottom) {
                padding.bottom = typedArray.getDimension(attr, 0f).toInt()
            } else if (attr == R.styleable.autoxml_shape_size_width) {
                sizeWidth = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_size_height) {
                sizeHeight = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_stroke_width) {
                strokeWidth = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_stroke_color) {
                strokeColor = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.autoxml_shape_stroke_dashWidth) {
                strokeDashWidth = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.autoxml_shape_stroke_dashGap) {
                strokeGap = typedArray.getDimension(attr, 0f)
            }
        }

        if (hasSetRadius(cornerRadius)) {
            drawable.cornerRadii = cornerRadius
        }
        if (typedArray.hasValue(R.styleable.autoxml_shape_size_width) &&
            typedArray.hasValue(R.styleable.autoxml_shape_size_height)
        ) {
            drawable.setSize(sizeWidth.toInt(), sizeHeight.toInt())
        }

        //设置填充颜色
        if (typedArray.hasValue(R.styleable.autoxml_shape_solid_color)) {
            drawable.setColor(solidColor)
        }

        //设置边框颜色
        if (typedArray.hasValue(R.styleable.autoxml_shape_stroke_color)) {
            drawable.setStroke(strokeWidth.toInt(), strokeColor, strokeDashWidth, strokeGap)
        }

        if (typedArray.hasValue(R.styleable.autoxml_shape_gradient_centerX) &&
            typedArray.hasValue(R.styleable.autoxml_shape_gradient_centerY)
        ) {
            drawable.setGradientCenter(centerX, centerY)
        }

        if (typedArray.hasValue(R.styleable.autoxml_shape_gradient_startColor) &&
            typedArray.hasValue(R.styleable.autoxml_shape_gradient_endColor)
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        ) {
            val colors: IntArray
            if (typedArray.hasValue(R.styleable.autoxml_shape_gradient_centerColor)) {
                colors = IntArray(3)
                colors[0] = startColor
                colors[1] = centerColor
                colors[2] = endColor
            } else {
                colors = IntArray(2)
                colors[0] = startColor
                colors[1] = endColor
            }
            drawable.colors = colors
        }

        if (gradientType == GradientDrawable.LINEAR_GRADIENT &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
            (typedArray.hasValue(R.styleable.autoxml_shape_gradient_angle))
        ) {
            gradientAngle %= 360
            if (gradientAngle % 45 != 0) {
                throw XmlPullParserException(
                    typedArray.positionDescription
                            + "<gradient> tag requires 'angle' attribute to "
                            + "be a multiple of 45"
                )
            }
            var mOrientation = GradientDrawable.Orientation.LEFT_RIGHT
            when (gradientAngle) {
                0 -> mOrientation = GradientDrawable.Orientation.LEFT_RIGHT
                45 -> mOrientation = GradientDrawable.Orientation.BL_TR
                90 -> mOrientation = GradientDrawable.Orientation.BOTTOM_TOP
                135 -> mOrientation = GradientDrawable.Orientation.BR_TL
                180 -> mOrientation = GradientDrawable.Orientation.RIGHT_LEFT
                225 -> mOrientation = GradientDrawable.Orientation.TR_BL
                270 -> mOrientation = GradientDrawable.Orientation.TOP_BOTTOM
                315 -> mOrientation = GradientDrawable.Orientation.TL_BR
            }
            drawable.orientation = mOrientation
        }

        if (typedArray.hasValue(R.styleable.autoxml_shape_padding_left) &&
            typedArray.hasValue(R.styleable.autoxml_shape_padding_top) &&
            typedArray.hasValue(R.styleable.autoxml_shape_padding_right) &&
            typedArray.hasValue(R.styleable.autoxml_shape_padding_bottom)
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable.setPadding(padding.left, padding.top, padding.right, padding.bottom)
            } else {
                try {
                    val paddingField = drawable.javaClass.getDeclaredField("mPadding")
                    paddingField.isAccessible = true
                    paddingField[drawable] = padding
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
        return drawable

    }


    private fun hasSetRadius(radius: FloatArray): Boolean {
        var hasSet = false
        for (f in radius) {
            if (f != 0.0f) {
                hasSet = true
                break
            }
        }
        return hasSet
    }
}