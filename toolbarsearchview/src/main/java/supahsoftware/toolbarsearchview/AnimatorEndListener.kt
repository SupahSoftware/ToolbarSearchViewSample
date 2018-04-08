package supahsoftware.toolbarsearchview

import android.animation.Animator

class AnimatorEndListener(private val after: () -> Unit) : Animator.AnimatorListener {
    override fun onAnimationRepeat(p0: Animator?) = Unit
    override fun onAnimationEnd(p0: Animator?) = after()
    override fun onAnimationCancel(p0: Animator?) = Unit
    override fun onAnimationStart(p0: Animator?) = Unit
}