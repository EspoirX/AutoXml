package com.espoir.autoxmlib.dsl

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.espoir.autoxmlib.dsl.DrawableBuilder


/**
 * @date: 2021/4/19 5:05 PM
 * @param resId 文件资源id
 */
class ResourceBuilder(@DrawableRes val resId: Int) : DrawableBuilder {

    override fun build(): Drawable {
        return ContextCompat.getDrawable(DrawableBuilder.cxt(), resId)!!
    }

    init {
        require(resId != 0) { "Id can not be 0" }
    }
}