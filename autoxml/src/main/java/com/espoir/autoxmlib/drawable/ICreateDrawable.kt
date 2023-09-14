package com.espoir.autoxmlib.drawable

import android.graphics.drawable.Drawable

interface ICreateDrawable {
    @Throws(Exception::class)
    fun create(): Drawable?
}