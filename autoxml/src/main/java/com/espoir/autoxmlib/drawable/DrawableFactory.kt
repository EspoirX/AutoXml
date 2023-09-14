package com.espoir.autoxmlib.drawable

import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import androidx.annotation.AttrRes
import org.xmlpull.v1.XmlPullParserException

object DrawableFactory {
    //获取shape属性的drawable
    @JvmStatic
    @Throws(XmlPullParserException::class)
    fun getDrawable(typedArray: TypedArray): GradientDrawable {
        return GradientDrawableCreator(typedArray).create() as GradientDrawable
    }

    @JvmStatic
    @Throws(XmlPullParserException::class)
    fun getDrawable(typedArray: TypedArray, @AttrRes gradientState: Int): GradientDrawable {
        return GradientDrawableCreator(typedArray, gradientState).create() as GradientDrawable
    }
}