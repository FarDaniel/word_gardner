package aut.dipterv.word_gardner

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import aut.dipterv.word_gardner.databinding.ActivityMainBinding
import aut.dipterv.word_gardner.interfaces.PopupReadyViewModel
import aut.dipterv.word_gardner.network_data.interceptors.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    val viewModel: MainViewModel by viewModels()
    var popupVisibility = View.GONE
    val loggedIn = MutableLiveData(Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.activity = this

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        initListeners()
    }

    private fun initListeners() {
        binding.mainLoginCard.goOffline.onEach {
            SessionManager(this).saveIsOnline(false)
            navigateOffline()
        }.launchIn(lifecycleScope)
        binding.mainLoginCard.registerUser.observe(this) {
            viewModel.register(it, this)
        }
        binding.mainLoginCard.loginUser.observe(this) {
            viewModel.login(it, this)
        }
        viewModel.error.observe(this) { errorCode ->
            binding.mainLoginCard.resetView()
            when (errorCode) {
                409 -> {
                    binding.mainLoginCard.setNameError("A felhasználónév foglalt.")
                }
                404 -> {
                    binding.mainLoginCard.setNameError("Hibás felhasználónév.")
                }
                401 -> {
                    binding.mainLoginCard.setPasswordError("Hibás jelszó.")
                }
                else -> {
                    binding.mainLoginCard.setNameError("Ismeretlen hiba,\npróbálkozz újra később.")
                }
            }
        }
        viewModel.loggedIn.observe(this) {
            binding.mainLoginCard.resetView()
            binding.mainLoginCard.disappear()
            loggedIn.value = Unit
        }
    }

    fun setPopupState(popupState: Boolean) {
        popupVisibility = if (popupState) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.activity = this
        binding.executePendingBindings()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setPopupState(false)
        binding.mainLoginCard.disappear()
    }

    fun setActiveViewModel(viewModel: PopupReadyViewModel) {
        binding.activeViewModel = viewModel
        binding.executePendingBindings()
    }

    fun popupLogin() {
        binding.mainLoginCard.appear()
    }

    fun navigateOffline() {
        navController.navigate(NavGraphDirections.actionToLanding())
    }

}
