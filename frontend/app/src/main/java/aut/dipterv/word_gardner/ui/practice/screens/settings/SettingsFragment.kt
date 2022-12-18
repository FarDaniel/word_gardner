package aut.dipterv.word_gardner.ui.practice.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    val viewModel: SettingsViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
        navController = this.findNavController()
        context?.let { context ->
            if (viewModel.getIsOnline(context)) {
                binding.online.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.green_border, null)
                binding.onlineButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.green,
                        null
                    )
                )
                binding.offline.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.border, null)
                binding.offlineButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
            } else {
                binding.offline.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.green_border, null)
                binding.offlineButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.green,
                        null
                    )
                )
                binding.online.foreground =
                    ResourcesCompat.getDrawable(resources, R.drawable.border, null)
                binding.onlineButton.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
            }
        }
    }

    fun logout() {
        context?.let { context ->
            viewModel.logout(context)
        }
        navController.popBackStack()
    }

    fun setOnline() {
        context?.let { context ->
            viewModel.setIsOnline(context, true)
            binding.online.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.green_border, null)
            binding.onlineButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.green,
                    null
                )
            )
            binding.offline.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.border, null)
            binding.offlineButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
        }
    }

    fun setOffline() {
        context?.let { context ->
            viewModel.setIsOnline(context, false)
            binding.offline.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.green_border, null)
            binding.offlineButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.green,
                    null
                )
            )
            binding.online.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.border, null)
            binding.onlineButton.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
        }
    }

}
