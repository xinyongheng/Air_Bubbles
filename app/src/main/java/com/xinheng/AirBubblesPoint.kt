package com.xinheng

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.util.Log

/**
 * Created by xinheng on 2019/12/12 12:08
 * describe:气泡信息
 */
class AirBubblesPoint(private var length: Int) {
    companion object {
        //最小圆心
        const val minR = 5
    }

    private val TAG = "TAG_AirPoint"
    var x = -1f
    var y = 0f
    var r = minR.toFloat()
    var duration = 0
    //动画进度（0->1）
    var progress = 0f
    fun finallyY(): Float = length - progress * length
    fun finallyX(): Float = x
    fun finallyR(): Float = progress * (r - minR) + minR
    //属性动画
    private val animator = ObjectAnimator.ofFloat(this, "value", 0f, 1f).apply {
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.RESTART
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator?) {
                super.onAnimationRepeat(animation)
                //单次动画执行完毕后，重设圆信息
                resetXY()
            }
        })
    }

    /**
     * 信息重设
     */
    private fun resetXY() {
        airBubblesListener?.let {
            x = it.resetX()
            r = it.resetR()
            y = it.resetY() + r
        }
    }

    var airBubblesListener: AirBubblesListener? = null
        set(value) {
            field = value
            if (x < 0) {
                resetXY()
            }
        }

    /**
     * 信息更新数据来源
     */
    interface AirBubblesListener {
        fun resetX(): Float
        fun resetY(): Float
        fun resetR(): Float
    }

    fun setValue(value: Float) {
        progress = value
    }

    /**
     * 开启动画
     */
    fun start() {
        animator.duration = duration.toLong()
        animator.start()
    }

    //AirBubblesPoint
    fun recycle() {
        animator.cancel()
        airBubblesListener = null
    }


}