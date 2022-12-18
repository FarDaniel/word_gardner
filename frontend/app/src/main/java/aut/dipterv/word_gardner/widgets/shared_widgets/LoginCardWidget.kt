package aut.dipterv.word_gardner.widgets.shared_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.databinding.WidgetLoginCardBinding
import aut.dipterv.word_gardner.network_data.models.LoginModel
import kotlinx.coroutines.flow.MutableSharedFlow

class LoginCardWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    val binding = WidgetLoginCardBinding.inflate(LayoutInflater.from(context), this, true)
    var isExpanded = false
    val loginUser = MutableLiveData<LoginModel>()
    val registerUser = MutableLiveData<LoginModel>()
    val goOffline = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        binding.widget = this
        binding.loginCard.animate()
            .translationX(-1500F)
            .setDuration(0)
            .start()
    }

    fun disappear() {
        switchState(true)
        binding.loginCard.animate()
            .translationX(-1500F)
            .setDuration(500)
            .start()
        binding.loginCover.visibility = View.GONE
    }

    fun appear() {
        binding.loginCard.animate()
            .translationX(0F)
            .setDuration(500)
            .start()
        binding.loginCover.visibility = View.VISIBLE
    }

    fun goOffline() {
        disappear()
        goOffline.tryEmit(Unit)
    }

    fun okClicked() {
        if (isExpanded) {
            switchState(true)
            register()
        } else {
            login()
        }
    }

    private fun login() {
        if (binding.userName.text?.isNotEmpty() == true) {
            if (binding.password.text?.isNotEmpty() == true) {
                loginUser.value = LoginModel(
                    userName = binding.userName.text.toString(),
                    password = binding.password.text.toString()
                )
                binding.userNameTextView.error = null
                binding.passwordTextView.error = null
            } else {
                binding.passwordTextView.error = "Nincs jelszó."
                binding.userNameTextView.error = null
            }
        } else {
            binding.userNameTextView.error = "Nincs felhasználónév."
            binding.passwordTextView.error = null
        }
    }

    private fun register() {
        if (binding.password.text.toString() == binding.passwordAgain.text.toString()) {
            registerUser.value = LoginModel(
                userName = binding.userName.text.toString(),
                password = binding.password.text.toString()
            )
            binding.passwordAgainTextView.error = null
        } else {
            binding.passwordAgainTextView.error = "Különböznek a jelszavak."
        }
    }

    fun switchState() {
        isExpanded = !isExpanded
        binding.loginCard.animate()
            .scaleX(0f)
            .setDuration(300)
            .withEndAction {
                resetView()
                updateViews()
                binding.loginCard.animate()
                    .scaleX(1f)
                    .setDuration(300)
                    .start()
            }.start()
    }

    fun setNameError(errorText: String?) {
        binding.userNameTextView.error = errorText
        binding.executePendingBindings()
    }

    fun setPasswordError(errorText: String?) {
        binding.passwordTextView.error = errorText
        binding.executePendingBindings()
    }

    fun resetView() {
        binding.userName.setText("")
        binding.password.setText("")
        binding.passwordAgain.setText("")
        binding.userNameTextView.error = null
        binding.passwordTextView.error = null
        binding.passwordAgainTextView.error = null
    }

    private fun switchState(toLogin: Boolean) {
        isExpanded = !toLogin
        binding.loginCard.animate()
            .scaleX(0f)
            .setDuration(300)
            .withEndAction {
                resetView()
                updateViews()
                binding.loginCard.animate()
                    .scaleX(1f)
                    .setDuration(300)
                    .start()
            }.start()
    }

    private fun updateViews() {
        if (isExpanded) {
            binding.passwordAgainTextView.visibility = View.VISIBLE
            binding.primerButton.text = "Regisztráció"
            binding.secondaryButton.text = "Belépés"
        } else {
            binding.passwordAgainTextView.visibility = View.GONE
            binding.primerButton.text = "Belépés"
            binding.secondaryButton.text = "Regisztráció"
        }
        binding.executePendingBindings()
    }

}
