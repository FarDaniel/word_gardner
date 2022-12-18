package aut.dipterv.word_gardner.widgets.hints

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.animation.doOnEnd
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetHintHangedManBinding
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType
import kotlinx.coroutines.*

class HangedManHint @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0
) : HintWidget(context, attrs, defStyleRes, HintType.HANGED_MAN_HINT) {
    private var charactersLeft = -1
    private var characters = HashMap<Char, Boolean>()
    var isRevealing = false

    private val binding =
        WidgetHintHangedManBinding.inflate(LayoutInflater.from(context), this, true)

    override val showAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.hint_show).apply {
            setTarget(binding.root)
            this.doOnEnd {
                stopRevealing()
                startRevealing()
            }
        }
    override val hideAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.hint_hide).apply {
            setTarget(binding.root)
            this.doOnEnd {
                closed.value = hintType
            }
        }

    init {
        binding.widget = this
    }

    private val viewScope: CoroutineScope
        get() {
            val storedScope = getTag(R.string.view_coroutine_scope) as? CoroutineScope
            if (storedScope != null) return storedScope

            val newScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
            if (isAttachedToWindow) {
                addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(view: View) = Unit

                    override fun onViewDetachedFromWindow(view: View) {
                        isRevealing = false
                        viewScope.cancel()
                        view.setTag(R.string.view_coroutine_scope, null)
                    }
                })
                setTag(R.string.view_coroutine_scope, newScope)
            } else newScope.cancel()

            return newScope
        }

    private lateinit var coroutineJob: Job

    override fun loadRandomWord() {
        super.loadRandomWord()

        for (actualCharacter: Char in (if (isNative) actualWordCard.nativeWord else actualWordCard.foreignWord)) {
            characters[actualCharacter] = false
        }
        charactersLeft = characters.size
        refresh()

        startRevealing()
    }

    override fun reset() {
        super.reset()

        stopRevealing()
        charactersLeft = -1
        characters.clear()
    }

    override fun close() {
        hideAnimation.start()
    }

    private fun refresh() {
        var toDisplay = ""
        val word = if (isNative) actualWordCard.nativeWord else actualWordCard.foreignWord
        for (char: Char in word) {
            if (characters[char] == true) {
                toDisplay += "$char "
            } else {
                toDisplay += "__ "
            }
        }
        if (charactersLeft == 0) {
            binding.textHintWord.text = word
        } else {
            binding.textHintWord.text = toDisplay.trim()
        }
        binding.executePendingBindings()
    }

    private fun revealCharacter() {
        val characters = ArrayList<Char>()
        val word = (if (isNative) actualWordCard.nativeWord else actualWordCard.foreignWord)
        if (word.length < word.toCharArray().size) {
            binding.textHintWord.text = word
            stopRevealing()
            binding.executePendingBindings()
        } else {
            for (char: Char in word) {
                if (this.characters[char] == false) {
                    characters.add(char)
                }
            }
            if (characters.isEmpty()) {
                stopRevealing()
            } else {
                characters.shuffle()
                this.characters[characters.first()] = true
                charactersLeft--
                refresh()
            }
        }
    }

    private fun startRevealing() {
        if (!isRevealing) {
            coroutineJob = viewScope.launch {
                while (true) {
                    delay(1500)
                    revealCharacter()
                }
            }
            isRevealing = true
        }
    }

    private fun stopRevealing() {
        if (isRevealing) {
            coroutineJob.cancel()
            isRevealing = false
        }
    }

    fun headerClicked() {
        revealCharacter()
        startRevealing()
    }

}
