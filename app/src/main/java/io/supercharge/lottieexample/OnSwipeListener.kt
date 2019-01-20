package io.supercharge.lottieexample

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.absoluteValue

private const val SWIPE_THRESHOLD = 100
private const val SWIPE_VELOCITY_THRESHOLD = 100

abstract class OnSwipeListener(ctx: Context) : View.OnTouchListener {

    private val gestureDetector = GestureDetector(ctx, GestureListener())

    override fun onTouch(v: View, event: MotionEvent) = gestureDetector.onTouchEvent(event)

    protected abstract fun onSwipeRight()

    protected abstract fun onSwipeLeft()

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent) = true

        override fun onFling(e1: MotionEvent, e2: MotionEvent,
                             velocityX: Float, velocityY: Float): Boolean {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x

            if (diffX.absoluteValue > diffY.absoluteValue
                    && diffX.absoluteValue > SWIPE_THRESHOLD
                    && velocityX.absoluteValue > SWIPE_VELOCITY_THRESHOLD) {

                if (diffX > 0) {
                    onSwipeRight()
                } else {
                    onSwipeLeft()
                }

                return true
            }

            return false
        }
    }
}
