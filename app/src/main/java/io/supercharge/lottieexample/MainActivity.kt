package io.supercharge.lottieexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.actualSpeedValue
import kotlinx.android.synthetic.main.activity_main.circleIndicator
import kotlinx.android.synthetic.main.activity_main.lottieAnimationView

private const val FIRST_STEP_END_FRACTION = 0.46f
private const val SECOND_STEP_END_FRACTION = 0.71f
private const val THIRD_STEP_END_FRACTION = 0.99f

private const val STEP_INDEX_FIRST = 0
private const val STEP_INDEX_SECOND = 1
private const val STEP_INDEX_THIRD = 2

private const val ANIMATION_INCREASED_SPEED = 4f

class MainActivity : AppCompatActivity() {

    private var targetFraction = -1f
    private var reversed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lottieAnimationView.setOnTouchListener(object : OnSwipeListener(applicationContext) {

            override fun onSwipeRight() = handleSpeedChanged(ANIMATION_INCREASED_SPEED.unaryMinus())

            override fun onSwipeLeft() = handleSpeedChanged(ANIMATION_INCREASED_SPEED)
        })

        lottieAnimationView.addAnimatorUpdateListener { animation ->
            val step = if (reversed) {
                getAnimationStepFromFraction(1 - animation.animatedFraction)
            } else {
                getAnimationStepFromFraction(animation.animatedFraction)
            }

            if (circleIndicator.selectedItem != step) {
                circleIndicator.selectedItem = step
            }

            if (targetFraction <= animation.animatedFraction) {
                handleSpeedChanged(1f)
            }
        }
    }

    private fun handleSpeedChanged(speed: Float) {
        reversed = speed < 0

        if (!lottieAnimationView.isAnimating) {
            if (reversed) {
                lottieAnimationView.speed = speed
                lottieAnimationView.playAnimation()
            } else {
                lottieAnimationView.playAnimation()
                lottieAnimationView.speed = speed
            }
        } else {
            lottieAnimationView.speed = speed
        }

        targetFraction = getTargetFraction()
        actualSpeedValue.text = getString(R.string.current_speed_label, speed.toInt())
    }

    private fun getTargetFraction() = with(lottieAnimationView) {
        when {
            reversed && progress > SECOND_STEP_END_FRACTION -> 1 - FIRST_STEP_END_FRACTION
            !reversed && progress < FIRST_STEP_END_FRACTION -> FIRST_STEP_END_FRACTION
            !reversed && progress < SECOND_STEP_END_FRACTION -> SECOND_STEP_END_FRACTION
            else -> THIRD_STEP_END_FRACTION
        }
    }

    private fun getAnimationStepFromFraction(fraction: Float): Int {
        return when {
            fraction < FIRST_STEP_END_FRACTION -> STEP_INDEX_FIRST
            fraction < SECOND_STEP_END_FRACTION -> STEP_INDEX_SECOND
            else -> STEP_INDEX_THIRD
        }
    }
}
