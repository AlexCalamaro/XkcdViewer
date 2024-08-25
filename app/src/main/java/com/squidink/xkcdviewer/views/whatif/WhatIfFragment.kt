package com.squidink.xkcdviewer.views.whatif

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.databinding.FragmentWhatIfBinding
import com.squidink.xkcdviewer.extensions.setBottomInset
import com.squidink.xkcdviewer.extensions.setTopInset

class WhatIfFragment : Fragment() {
    private lateinit var webView: WebView
    private var binding: FragmentWhatIfBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWhatIfBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setInsets()
        setButtons()

        // Configure WebView
        webView = view.findViewById(R.id.what_if_webview)
        webView.webViewClient = WhatIfWebViewClient(binding)
        webView.settings.apply {
            javaScriptEnabled = true
        }
        webView.loadUrl(getString(R.string.what_if_url))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setInsets() {
        binding?.let {
            setTopInset(binding!!.whatIfToolbar)
            setBottomInset(binding!!.whatIfWebview)
        }
    }

    private fun setButtons() {
        // Nav button always goes back
        binding?.whatIfToolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        // Back nav goes back in webview as far as it can
        requireActivity().onBackPressedDispatcher.addCallback {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                findNavController().navigateUp()
            }
        }
    }
}