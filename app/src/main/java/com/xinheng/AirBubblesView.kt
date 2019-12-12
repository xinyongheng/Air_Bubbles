package com.xinheng

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.xinheng.AirBubblesPoint.Companion.minR
import java.util.*

/**
 * Created by xinheng on 2019/12/12 10:52
 * describe:好多气泡
 */
class AirBubblesView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val TAG = "TAG_AirView"
    private val linkListPoint = LinkedList<AirBubblesPoint>()
    private val random = Random(10)
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isDither = true
        isAntiAlias = true
    }
    /**
     * 动画开启标志
     */
    private var animationStartTag = false
    private val animation = ValueAnimator().apply {
        setTarget(this@AirBubblesView)
        setFloatValues(0f, 2f)
        duration = 1000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        addUpdateListener {
            if (linkListPoint.size < 31) {//数量限制
                addAirPoint()
            }
            invalidate()
        }
    }

    init {
        paint.color = Color.WHITE
        paint.alpha = 60
        paint.style = Paint.Style.FILL
        //背景设置
        background = ContextCompat.getDrawable(context, R.drawable.drawable_bg)
    }

    /**
     * 添加气泡，并开启动画
     */
    private fun addAirPoint() {
        val airPoint = createAirPoint()
        airPoint.duration = (random.nextInt(7) + 4) * 1000
        linkListPoint.addLast(airPoint)
        airPoint.start()
    }

    private fun createAirPoint(): AirBubblesPoint = AirBubblesPoint(measuredHeight).apply {
        //接口绑定
        airBubblesListener = resetListener
    }

    /**
     * 设置接口返回值
     */
    private val resetListener = object : AirBubblesPoint.AirBubblesListener {
        override fun resetX(): Float = random.nextInt(measuredWidth).toFloat()
        override fun resetR(): Float = random.nextInt(40) + minR * 2f
        override fun resetY(): Float = measuredHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        if (!animationStartTag) {
            animationStartTag = true
            animation.start()
        }
        linkListPoint.forEach {
            val cx = it.finallyX()
            val cy = it.finallyY()
            //Log.e(TAG,"cx=$cx cy=$cy r=${it.r}")
            canvas.drawCircle(cx, cy, it.finallyR(), paint)
        }
    }

    //AirBubblesView
    override fun onDetachedFromWindow() {
        recycle()
        Log.e(TAG, "onDetachedFromWindow")
        super.onDetachedFromWindow()
    }
    fun recycle() {
        animation.cancel()
        linkListPoint.forEach {
            it.recycle()
        }
        linkListPoint.clear()
    }


}