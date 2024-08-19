package com.squidink.xkcdviewer.views.whatif

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.databinding.FragmentWhatIfBinding
import com.squidink.xkcdviewer.extensions.spinForever


class WhatIfWebViewClient(
    val binding: FragmentWhatIfBinding?
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        showLoading()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        view?.loadUrl("""
            javascript:function cleanPage() {
                var divNames = ["header-wrapper", "news-wrapper", "the-books", "the-comic", "footer-wrapper"];
                for (var i=0, n=divNames.length; i<n; i++) {
                   document.getElementById(divNames[i]).remove();
                }
                document.getElementById("entry-wrapper").style.marginTop='1em';
                document.body.style.background='white';
            } cleanPage();
        """.trimIndent())
        hideLoading()
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//        request.url.toString().let {
//            if(it.matches(Regex("https://what-if.xkcd.com/\\d+"))) {
//                return false
//            } else if(it == "https://what-if.xkcd.com/archive") {
//                return false
//            } else {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
//                view.context.startActivity(intent)
//                return true
//            }
//        }
        return false
    }

    private fun showLoading() {
        binding?.whatIfLoadingContainer?.visibility = View.VISIBLE
        binding?.whatIfLoadingImage?.spinForever()
    }

    private fun hideLoading() {
        binding?.whatIfLoadingContainer?.visibility = View.GONE
    }
}