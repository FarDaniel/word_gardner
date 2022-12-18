package aut.dipterv.word_gardner.widgets.hints

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.animation.doOnEnd
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetHintImageBinding
import aut.dipterv.word_gardner.network_data.UnsplashRetrofitFactory
import aut.dipterv.word_gardner.network_data.interactors.UnsplashInteractor
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.widget_hint_image.view.*

class ImageHint @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : HintWidget(context, attrs, defStyleRes, HintType.IMAGE_HINT) {
    private val unsplashInteractor: UnsplashInteractor =
        UnsplashRetrofitFactory.getUnsplashInteractor(context)

    private val binding =
        WidgetHintImageBinding.inflate(LayoutInflater.from(context), this, true)

    override val showAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.hint_show).apply {
            setTarget(binding.root)
        }
    override val hideAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.hint_hide).apply {
            setTarget(binding.root)
            this.doOnEnd {
                closed.value = hintType
            }
        }

    override fun close() {
        hideAnimation.start()
    }

    override fun loadRandomWord() {
        super.loadRandomWord()
        setWord(actualWordCard)
    }

    private fun setWord(wordCard: WordCardModel) {
        actualWordCard = wordCard
        var image = ""
        val pictureDisposable = unsplashInteractor.getPicture(wordCard.foreignWord).subscribe({
            it?.let {
                if (it.results.isNotEmpty()) {
                    image = it.results.first().urls.smallPictureUrl
                    Glide.with(context)
                        .load(image)
                        .into(binding.imageHintWord)
                } else {
                    binding.textHintWord.text = wordCard.foreignWord
                }
            }
            binding.imageHintWord.image_hint_word.contentDescription = wordCard.nativeWord
        }, {
            binding.textHintWord.text = wordCard.foreignWord
        })
        binding.executePendingBindings()
    }
}
