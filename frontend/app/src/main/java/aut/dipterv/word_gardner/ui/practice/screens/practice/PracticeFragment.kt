package aut.dipterv.word_gardner.ui.practice.screens.practice

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import aut.dipterv.word_gardner.MainActivity
import aut.dipterv.word_gardner.databinding.FragmentTestBinding
import aut.dipterv.word_gardner.interfaces.ActivePacksReader
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.InputToHintManager
import aut.dipterv.word_gardner.widgets.hints.HintWidget
import aut.dipterv.word_gardner.widgets.inputs.InputWidget
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class PracticeFragment : Fragment(), ActivePacksReader {
    @Inject
    lateinit var inputToHintManager: InputToHintManager
    val viewModel: PracticeViewModel by viewModels()
    lateinit var binding: FragmentTestBinding
    private lateinit var mainActivity: MainActivity
    private val args: PracticeFragmentArgs by navArgs()
    override var navController: NavController? = null
    override var fallbackDirection =
        PracticeFragmentDirections.actionTestFragmentToPackSelectorFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = this.findNavController()


        mainActivity.setActiveViewModel(viewModel)

        context?.let { context ->
            viewModel.loadCards(getActivePacks(context))
            val hints = inputToHintManager.makeHints(args.hint, context)
            val inputs = inputToHintManager.makeInputs(args.input, context)
            if (args.recommended) {
                viewModel.setupRecommendations(hints, inputs)
            }
        }

        viewModel.testStarted(10 + Random.nextInt(3) * 5)

        if (inputToHintManager.isMichrophoneNeeded) {
            requestRecordAudioPermission()
        }

        initListeners()
    }

    private fun setupHint(hint: HintWidget?) {
        if (hint != null) {
            binding.hintConstraintLayout.layoutParams.apply {
                hint.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
            }
            binding.hintConstraintLayout.removeAllViews()

            binding.hintConstraintLayout.addView(hint)
            binding.executePendingBindings()
        } else {
            viewModel.isPopupNeeded.value = true
        }
    }

    private fun setupInput(input: InputWidget?) {
        if (input != null) {
            binding.inputConstraintLayout.layoutParams.apply {
                input.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
            }
            binding.inputConstraintLayout.removeAllViews()

            binding.inputConstraintLayout.addView(input)
            binding.executePendingBindings()
        } else {
            viewModel.isPopupNeeded.value = true
        }
    }

    private fun initListeners() {
        viewModel.cards.observe(viewLifecycleOwner) {
            inputToHintManager.setWords(it)
            setupHint(inputToHintManager.switchHint())
            setupInput(inputToHintManager.switchInput())
        }
        viewModel.gratulationsText.observe(viewLifecycleOwner) {
            binding.resultsPopup.popUp(it)
        }
        viewModel.fallback.observe(viewLifecycleOwner) {
            navController?.navigate(
                PracticeFragmentDirections.actionTestFragmentToLandingPageFragment()
            )
        }
        binding.resultsPopup.restartClicked.observe(viewLifecycleOwner) {
            viewModel.testStarted(10 + Random.nextInt(3) * 5)
        }
        binding.resultsPopup.backClicked.observe(viewLifecycleOwner) {
            navController?.navigate(PracticeFragmentDirections.actionTestFragmentToLandingPageFragment())
        }
        inputToHintManager.getTipLiveDatas().forEach { liveData ->
            liveData.observe(viewLifecycleOwner) { tip ->
                viewModel.processSolution(tip, inputToHintManager.getTestIdentifier())
                if (tip.isCorrect) {
                    binding.blinker.blinkGreen()
                } else {
                    binding.blinker.blinkRed()
                }
                if (Random.nextBoolean()) {
                    inputToHintManager.closeHint()
                } else {
                    inputToHintManager.closeInput()
                }
            }
        }
        inputToHintManager.getCloseHintLiveDatas().forEach { liveData ->
            liveData.observe(viewLifecycleOwner) {
                setupHint(inputToHintManager.switchHint())
            }
        }
        inputToHintManager.getCloseInputLiveDatas().forEach { liveData ->
            liveData.observe(viewLifecycleOwner) {
                setupInput(inputToHintManager.switchInput())
            }
        }
        viewModel.isPopupNeeded.observe(viewLifecycleOwner) { isNeeded ->
            if (isNeeded) {
                mainActivity.setPopupState(true)
            } else {
                mainActivity.setPopupState(false)
            }
        }
        viewModel.reset.observe(viewLifecycleOwner) {
            navController?.navigate(
                PracticeFragmentDirections.actionTestFragmentSelf(
                    intArrayOf(),
                    intArrayOf(),
                    args.recommended
                )
            )
        }
        viewModel.setupRecommendations.observe(viewLifecycleOwner) {
            inputToHintManager.setupRecommendations(it)
        }
    }

    private fun requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val requiredPermission = Manifest.permission.RECORD_AUDIO

            context?.let { context ->
                activity?.let { activity ->
                    if (PermissionChecker.checkCallingOrSelfPermission(
                            context,
                            requiredPermission
                        ) == PermissionChecker.PERMISSION_DENIED
                    ) {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(requiredPermission),
                            101
                        )
                    }
                }
            }
        }
    }

}
