package com.acalamaro.xkcdviewer.views.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acalamaro.xkcdviewer.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment(), SettingsContract.View {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    lateinit var presenter: SettingsPresenter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        presenter = SettingsPresenter(this, SettingsModel())
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()
    }

    override fun setNotificationsToggleState() {
        TODO("Not yet implemented")
    }

    override fun setDarkModeImagesToggleState() {
        TODO("Not yet implemented")
    }

    private fun bindButtons() {
        binding.notificationsToggle.setOnClickListener {
            presenter.onNotificationsToggled()
        }
        binding.colorSettingsToggle.setOnClickListener {
            presenter.onDarkModeImagesToggled()
        }
    }

}