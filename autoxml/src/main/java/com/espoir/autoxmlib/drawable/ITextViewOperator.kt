package com.espoir.autoxmlib.drawable

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

interface ITextViewOperator {
    operator fun invoke(context: Context, attrs: AttributeSet, textView: TextView)
}