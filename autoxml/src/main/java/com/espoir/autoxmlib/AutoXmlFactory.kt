package com.espoir.autoxmlib

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import android.view.View
import android.widget.TextView
import androidx.collection.ArrayMap
import com.espoir.autoxml.R
import com.espoir.autoxmlib.drawable.DrawableFactory
import com.espoir.autoxmlib.drawable.TextViewFactory
import java.lang.reflect.Constructor
import java.lang.reflect.Method

class AutoXmlFactory : Factory2 {
    private var mViewCreateFactory: LayoutInflater.Factory? = null
    private var mViewCreateFactory2: Factory2? = null
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var view: View? = null

        //防止与其他调用factory库冲突，例如字体、皮肤替换库，用已经设置的factory来创建view
        if (mViewCreateFactory2 != null) {
            view = mViewCreateFactory2?.onCreateView(name, context, attrs)
            if (view == null) {
                view = mViewCreateFactory2!!.onCreateView(null, name, context, attrs)
            }
        } else if (mViewCreateFactory != null) {
            view = mViewCreateFactory?.onCreateView(name, context, attrs)
        }
        return setViewBackground(name, context, attrs, view)
    }

    fun setInterceptFactory(factory: LayoutInflater.Factory?) {
        mViewCreateFactory = factory
    }

    fun setInterceptFactory2(factory: Factory2?) {
        mViewCreateFactory2 = factory
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(name, context, attrs)
    }

    companion object {
        private val sConstructorSignature = arrayOf(
            Context::class.java, AttributeSet::class.java
        )
        private val mConstructorArgs = arrayOfNulls<Any>(2)
        private val sConstructorMap: MutableMap<String?, Constructor<out View>?> = ArrayMap()
        private val methodMap = HashMap<String, HashMap<String, Method>>()
        fun setViewBackground(context: Context, attrs: AttributeSet, view: View?): View? {
            return setViewBackground(null, context, attrs, view)
        }

        /**
         * 根据属性设置图片背景
         *
         * @param name    view的名字
         * @param context 上下文
         * @param attrs   bl属性
         * @param view    view
         * @return view
         */
        private fun setViewBackground(name: String?, context: Context, attrs: AttributeSet, v: View?): View? {
            var view = v
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.autoxml)
            val textViewTa = context.obtainStyledAttributes(attrs, R.styleable.shape_text)
            return try {
                if (typedArray.indexCount == 0 && textViewTa.indexCount == 0) {
                    return view
                }
                if (view == null) {
                    view = createViewFromTag(context, name, attrs)
                }
                if (view == null) {
                    return null
                }
                var drawable: GradientDrawable? = null
                var stateListDrawable: StateListDrawable? = null
                if (typedArray.indexCount > 0) {
                    drawable = DrawableFactory.getDrawable(typedArray)
                    setBackground(drawable, view, typedArray)
                }
                if (view is TextView && textViewTa.indexCount > 0) {
                    TextViewFactory.setTextGradientColor(context, attrs, view)
                }
                if (typedArray.getBoolean(R.styleable.autoxml_shape_ripple_enable, false) &&
                    typedArray.hasValue(R.styleable.autoxml_shape_ripple_color)
                ) {
                    val color = typedArray.getColor(R.styleable.autoxml_shape_ripple_color, 0)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val contentDrawable = stateListDrawable ?: drawable
                        val rippleDrawable = RippleDrawable(
                            ColorStateList.valueOf(color), contentDrawable, contentDrawable
                        )
                        setBackground(rippleDrawable, view, typedArray)
                    } else if (stateListDrawable == null) {
                        val tmpDrawable = StateListDrawable()
                        val unPressDrawable = DrawableFactory.getDrawable(typedArray)
                        unPressDrawable.setColor(color)
                        tmpDrawable.addState(intArrayOf(-android.R.attr.state_pressed), drawable)
                        tmpDrawable.addState(intArrayOf(android.R.attr.state_pressed), unPressDrawable)
                        setBackground(tmpDrawable, view, typedArray)
                    }
                }
                view
            } catch (e: Exception) {
                e.printStackTrace()
                view
            } finally {
                typedArray.recycle()
                textViewTa.recycle()
            }
        }

        private fun setBackground(d: Drawable, view: View, typedArray: TypedArray) {
            var drawable = d
            if (typedArray.hasValue(R.styleable.autoxml_shape_stroke_width) && typedArray.hasValue(R.styleable.autoxml_shape_stroke_position)) {
                val left = 1 shl 1
                val top = 1 shl 2
                val right = 1 shl 3
                val bottom = 1 shl 4
                val width = typedArray.getDimension(R.styleable.autoxml_shape_stroke_width, 0f)
                val position = typedArray.getInt(R.styleable.autoxml_shape_stroke_position, 0)
                val leftValue: Float = if (hasStatus(position, left)) 0f else -width
                val topValue: Float = if (hasStatus(position, top)) 0f else -width
                val rightValue: Float = if (hasStatus(position, right)) 0f else -width
                val bottomValue: Float = if (hasStatus(position, bottom)) 0f else -width
                drawable = LayerDrawable(arrayOf(drawable))
                drawable.setLayerInset(0, leftValue.toInt(), topValue.toInt(), rightValue.toInt(), bottomValue.toInt())
            }
            if (typedArray.hasValue(R.styleable.autoxml_shape_alpha)) {
                var alpha = typedArray.getFloat(R.styleable.autoxml_shape_alpha, 0f)
                alpha = if (alpha >= 1) {
                    255f
                } else if (alpha <= 0) {
                    0f
                } else {
                    alpha * 255
                }
                drawable.alpha = alpha.toInt()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.background = drawable
            } else {
                view.setBackgroundDrawable(drawable)
            }
        }

        private fun hasStatus(flag: Int, status: Int): Boolean {
            return flag and status == status
        }

        private fun createViewFromTag(context: Context, b: String?, attrs: AttributeSet): View? {
            var name = b
            if (TextUtils.isEmpty(name)) {
                return null
            }
            if (name == "view") {
                name = attrs.getAttributeValue(null, "class")
            }
            return try {
                mConstructorArgs[0] = context
                mConstructorArgs[1] = attrs
                if (-1 == name!!.indexOf('.')) {
                    var view: View? = null
                    if ("View" == name) {
                        view = createView(context, name, "android.view.")
                    }
                    if (view == null) {
                        view = createView(context, name, "android.widget.")
                    }
                    if (view == null) {
                        view = createView(context, name, "android.webkit.")
                    }
                    view
                } else {
                    createView(context, name, null)
                }
            } catch (e: Exception) {
                Log.w("AutoXml", "cannot create 【$name】 : ")
                null
            } finally {
                mConstructorArgs[0] = null
                mConstructorArgs[1] = null
            }
        }

        @Throws(InflateException::class)
        private fun createView(context: Context, name: String?, prefix: String?): View? {
            var constructor = sConstructorMap[name]
            return try {
                if (constructor == null) {
                    val clazz = context.classLoader.loadClass(
                        if (prefix != null) prefix + name else name
                    ).asSubclass(View::class.java)
                    constructor = clazz.getConstructor(*sConstructorSignature)
                    sConstructorMap[name] = constructor
                }
                constructor?.isAccessible = true
                constructor?.newInstance(*mConstructorArgs)
            } catch (e: Exception) {
                Log.w("AutoXml", "cannot create 【$name】 : ")
                null
            }
        }
    }
}