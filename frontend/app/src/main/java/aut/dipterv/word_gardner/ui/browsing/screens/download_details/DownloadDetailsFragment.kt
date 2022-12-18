package aut.dipterv.word_gardner.ui.browsing.screens.download_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import aut.dipterv.word_gardner.MainActivity
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.FragmentDownloadDetailsBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.network_data.models.dtos.SearchFilter
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.widgets.shared_widgets.OnlineSpinnerWidget
import aut.dipterv.word_gardner.widgets.shared_widgets.SearchSwipeScreenWidget
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_votes.view.*
import kotlinx.android.synthetic.main.widget_number_picker_popup.view.*

@AndroidEntryPoint
class DownloadDetailsFragment : Fragment(), SearchPackNavigator {
    val viewModel: DownloadDetailsViewModel by viewModels()
    private lateinit var binding: FragmentDownloadDetailsBinding
    private lateinit var navController: NavController
    private val args: DownloadDetailsFragmentArgs by navArgs()
    private lateinit var swipeScreen: SearchSwipeScreenWidget

    var isUpVoted = false
    var isDownVoted = false

    private lateinit var spinner: OnlineSpinnerWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadDetailsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.type = args.type
        viewModel.id = args.id
        navController = findNavController()
        viewModel.init()
        swipeScreen = binding.searchSwipeScreenWidget
        binding.fragment = this

        spinner = binding.downloadSpinner

        initListeners()
    }

    override fun onResume() {
        super.onResume()


        spinner.reset()
    }

    fun download() {
        viewModel.download()
    }

    private fun initListeners() {
        swipeScreen.leftContentNeeded.observe(viewLifecycleOwner) {
            viewModel.getPrevious()
        }
        swipeScreen.rightContentNeeded.observe(viewLifecycleOwner) {
            viewModel.getNext()
        }
        binding.typeSearchExtendableNumberPicker.chosenNumber.observe(viewLifecycleOwner) {
            viewModel.jumpTo((it ?: 1) - 1)
        }
        binding.votesView.upvote_button.setOnClickListener {
            if (isUpVoted) {
                isUpVoted = false
                isDownVoted = false
            } else {
                isUpVoted = true
                isDownVoted = false
            }

            viewModel.vote(isUpVoted, isDownVoted)
        }
        binding.votesView.downvote_button.setOnClickListener {
            if (isDownVoted) {
                isUpVoted = false
                isDownVoted = false
            } else {
                isUpVoted = false
                isDownVoted = true
            }

            viewModel.vote(isUpVoted, isDownVoted)
        }
        viewModel.previousModels.observe(viewLifecycleOwner) { models ->
            swipeScreen.setLeft(models)
        }
        viewModel.nextModels.observe(viewLifecycleOwner) { models ->
            swipeScreen.setRight(models)
        }
        viewModel.initialModels.observe(viewLifecycleOwner) { models ->
            swipeScreen.init(
                models ?: arrayListOf(),
                viewModel.type,
                this
            )
        }
        viewModel.allPage.observe(viewLifecycleOwner) {
            val allPages = (it ?: 1)
            binding.allPagesTextview.text = allPages.toString()
            binding.typeSearchExtendableNumberPicker.numberPicker.maxValue = allPages
        }
        viewModel.actualPage.observe(viewLifecycleOwner) {
            val actualPage = ((it ?: 0) + 1)
            binding.actualPageTextview.text = actualPage.toString()
            binding.typeSearchExtendableNumberPicker.numberPicker.value = actualPage
        }
        viewModel.toJumpMainModels.observe(viewLifecycleOwner) {
            swipeScreen.loadMainPage(it ?: listOf())
        }
        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user.name.isNotEmpty()) {
                spinner.disappear()
            }
            binding.userName.text = user.name
            if (!user.picture.isNullOrEmpty()) {
                Glide.with(binding.root)
                    .load(user.picture)
                    .into(binding.profilePictureImageView)
            }
            binding.executePendingBindings()
        }
        viewModel.name.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                spinner.disappear()
            }
            binding.name.text = it
            binding.executePendingBindings()
        }
        viewModel.upVotes.observe(viewLifecycleOwner) {
            binding.votesView.upvote_number.text = shortenNumberStringHun(it)
            binding.executePendingBindings()
        }
        viewModel.downVotes.observe(viewLifecycleOwner) {
            binding.votesView.downvote_number.text = shortenNumberStringHun(it)
            binding.executePendingBindings()
        }
        viewModel.fault.observe(viewLifecycleOwner) {
            spinner.popup("Hálózati hiba történt, próbáld újra később.")
        }
        viewModel.login.observe(viewLifecycleOwner) {
            (activity as MainActivity).popupLogin()
        }
        (activity as MainActivity).loggedIn.observe(viewLifecycleOwner) {

        }
        viewModel.downloaded.observe(viewLifecycleOwner) {
            navController.navigate(DownloadDetailsFragmentDirections.actionDownloadDetailsFragmentToPackSelectorFragment())
        }
        viewModel.ownVote.observe(viewLifecycleOwner) {
            context?.let { context ->
                if (it != null) {
                    if (it.isUpvote) {
                        isUpVoted = true
                        isDownVoted = false
                        binding.votesView.upvote_button.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.green
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )

                        binding.votesView.downvote_button.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        isUpVoted = false
                        isDownVoted = true
                        binding.votesView.upvote_button.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )

                        binding.votesView.downvote_button.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.red
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    }
                } else {
                    isUpVoted = false
                    isDownVoted = false
                    binding.votesView.upvote_button.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                    binding.votesView.downvote_button.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }

            }
        }
        spinner.reloadNeeded.observe(viewLifecycleOwner) {
            viewModel.init()
            viewModel.loadHeader()
        }
        spinner.navigateBack.observe(viewLifecycleOwner) {
            (activity as MainActivity).navigateOffline()
        }
    }

    fun openNumberPicker() {
        binding.typeSearchExtendableNumberPicker.popup((viewModel.actualPage.value ?: 0) + 1)
    }

    fun navigateToUserDetails() {
        navigateToUserDetails(viewModel.user.value?.onlineId ?: -1)
    }

    override fun navigateToCategorySearch(type: SearchableModelBase.SearchableType) {
        throw Error("There is no way from here to category search.")
    }

    override fun navigateToUserDetails(userId: Long) {
        navController.navigate(
            DownloadDetailsFragmentDirections.actionDownloadDetailsFragmentToUserFragment(
                SearchFilter(userId = userId)
            )
        )
    }

    override fun navigateDownloadDetails(type: SearchableModelBase.SearchableType, id: Long) {
        navController.navigate(
            DownloadDetailsFragmentDirections.actionDownloadDetailsFragmentSelf(
                SearchableModelBase.SearchableType.TYPE_CARD,
                id
            )
        )
    }

    private fun shortenNumberStringHun(num: Int): String {
        return when {
            num in 1000..999999 -> {
                (num / 1000).toString() + "e"
            }
            num > 999999 -> {
                (num / 100000).toString() + "M"
            }
            else -> {
                num.toString()
            }
        }

    }

}
