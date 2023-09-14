package com.espoir.autoxmlib

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat

object AutoXmlManager {
    @JvmStatic
    fun inject(context: Context): LayoutInflater? {
        val inflater: LayoutInflater? = if (context is Activity) {
            context.layoutInflater
        } else {
            LayoutInflater.from(context)
        }
        if (inflater == null) {
            return null
        }
        if (inflater.factory2 == null) {
            val factory = setDelegateFactory(context)
            inflater.factory2 = factory
        } else if (inflater.factory2 !is AutoXmlFactory) {
            forceSetFactory2(inflater)
        }
        return inflater
    }

    private fun setDelegateFactory(context: Context): AutoXmlFactory {
        val factory = AutoXmlFactory()
        if (context is AppCompatActivity) {
            val delegate = context.delegate
            factory.setInterceptFactory { name, ctx, attrs -> delegate.createView(null, name, ctx, attrs) }
        }
        return factory
    }

    /**
     * used for activity which has addFactory
     * 如果因为其他库已经设置了factory，可以使用该方法去进行inject，在其他库的setFactory后面调用即可
     *
     * @param context
     */
    fun inject2(context: Context?): LayoutInflater? {
        val inflater: LayoutInflater? = if (context is Activity) {
            context.layoutInflater
        } else {
            LayoutInflater.from(context)
        }
        if (inflater == null) {
            return null
        }
        forceSetFactory2(inflater)
        return inflater
    }

    private fun forceSetFactory2(inflater: LayoutInflater) {
        val compatClass = LayoutInflaterCompat::class.java
        val inflaterClass = LayoutInflater::class.java
        try {
            val sCheckedField = compatClass.getDeclaredField("sCheckedField")
            sCheckedField.isAccessible = true
            sCheckedField.setBoolean(compatClass, false)
            val mFactory = inflaterClass.getDeclaredField("mFactory")
            mFactory.isAccessible = true
            val mFactory2 = inflaterClass.getDeclaredField("mFactory2")
            mFactory2.isAccessible = true
            val factory = AutoXmlFactory()
            if (inflater.factory2 != null) {
                factory.setInterceptFactory2(inflater.factory2)
            } else if (inflater.factory != null) {
                factory.setInterceptFactory(inflater.factory)
            }
            mFactory2[inflater] = factory
            mFactory[inflater] = factory
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }
}