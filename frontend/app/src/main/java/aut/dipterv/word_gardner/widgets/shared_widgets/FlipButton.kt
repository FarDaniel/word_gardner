package aut.dipterv.word_gardner.widgets.shared_widgets

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetFlipButtonBinding


class FlipButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {

    private val binding =
        WidgetFlipButtonBinding.inflate(LayoutInflater.from(context), this, true)

    val firstSideClicked = MutableLiveData<Unit>()
    val secondSideClicked = MutableLiveData<Unit>()
    private var rotateAnimation: ObjectAnimator = ObjectAnimator.ofFloat(
        binding.root, "scaleX", 1f,
        0f
    ).apply {
        duration = ANIMATION_DURATION
        startDelay = 0
        repeatCount = 0
    }
    private var rotateBackAnimation: ObjectAnimator = ObjectAnimator.ofFloat(
        binding.root, "scaleX", 0f,
        1f
    ).apply {
        duration = ANIMATION_DURATION
        startDelay = ANIMATION_DURATION
        repeatCount = 0
    }
    var isFirstSideActive = true
    private var firstSide: Drawable? = null
    private var secondSide: Drawable? = null

    companion object {
        private const val ANIMATION_DURATION = 150L
    }

    init {
        val costumeAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.FlipButton, defStyleRes, 0)
        try {
            firstSide = costumeAttributes.getDrawable(R.styleable.FlipButton_firstSide)
            secondSide = costumeAttributes.getDrawable(R.styleable.FlipButton_secondSide)
            binding.buttonFirstSide.setImageDrawable(firstSide)
            binding.executePendingBindings()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            costumeAttributes.recycle()
        }
        binding.widget = this
        rotateAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                isFirstSideActive = if (isFirstSideActive) {
                    binding.buttonFirstSide.visibility = View.GONE
                    binding.buttonSecondSide.visibility = View.VISIBLE
                    binding.buttonSecondSide.setImageDrawable(secondSide)
                    false
                } else {
                    binding.buttonFirstSide.visibility = View.VISIBLE
                    binding.buttonSecondSide.visibility = View.GONE
                    binding.buttonFirstSide.setImageDrawable(firstSide)
                    true
                }
                binding.executePendingBindings()
            }

            override fun onAnimationCancel(p0: Animator) {}

            override fun onAnimationStart(p0: Animator) {}

        })
    }

    fun flip() {
        rotateAnimation.start()
        rotateBackAnimation.start()
    }

    fun flip(toFirst: Boolean) {
        if (toFirst) {
            if (!isFirstSideActive) {
                flip()
            }
        } else {
            if (isFirstSideActive) {
                flip()
            }
        }
    }

    fun firstSideClicked() {
        firstSideClicked.value = Unit
    }

    fun secondSideClicked() {
        secondSideClicked.value = Unit
    }

}
