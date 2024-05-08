package com.squidink.xkcdviewer.views.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.databinding.FragmentSettingsBinding
import com.squidink.xkcdviewer.views.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = findNavController()
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()

        viewModel.uiState.observe(viewLifecycleOwner) { uiModel ->
            uiModel?.let {
                binding.let {
                    it.notificationsToggle.isChecked = uiModel.notificationsEnabled
                    it.darkModeToggle.isChecked = uiModel.darkModeEnabled
                }
            }
        }
    }

    private fun bindButtons() {
        binding.notificationsToggle.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onNotificationsToggled(isChecked)
        }
        binding.darkModeToggle.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onDarkModeToggled(isChecked)
        }
        binding.donateButton.setOnClickListener {
            // Open chrome tab to donate
            Intent(Intent.ACTION_VIEW).apply {
                data = getString(R.string.donation_url).toUri()
                startActivity(this)
            }
        }
        binding.aboutText.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }
}