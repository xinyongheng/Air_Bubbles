package com.xinheng

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.random.Random

/**
 * Created by xinheng on 2019/12/12 10:52
 * describe:一个圆 前部
 */
class AirBubbles1View @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**
     * 画笔
     */
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 初始化球半径
     */
    private var r = 10f
    //圆心坐标x
    private var xAir = 200f
    //圆心坐标y
    private var yAir = -1f
    //随机
    private val random = Random(10)

    init {
        paint.isDither = true
        paint.isAntiAlias = true
        //设置背景
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
    }

    //开始绘制
    override fun onDraw(canvas: Canvas) {
        //因为没有自己计算view大小（重写onMeasure()方法），在onDraw初始化
        if (yAir < 0) {
            //刚开始底部Y轴坐标
            val startY = measuredHeight + r
            //刚开始X轴坐标
            val startX = 200f
            yAir = startY
            xAir = startX
            startAnimal()
        }
        canvas.drawCircle(xAir, yAir, r, paint)
    }
    private fun startAnimal() {
        val animal = ObjectAnimator.ofFloat(this, "transport", 0f, 1f)
        animal.duration = 8000
        animal.start()
        animal.repeatMode = ValueAnimator.RESTART
        animal.repeatCount = ValueAnimator.INFINITE
    }
    fun setTransport(arg: Float) {
        val startX = 200f
        //x轴添加了抖动（此数值效果不是很好，没接着试，直接去掉了）
        val x = startX + (random.nextFloat() - .5f) * 2.3f
        r = arg * 30 + 10
        xAir = x
        yAir = measuredHeight - measuredHeight * arg
        invalidate()
    }


}