package com.h5wrapper

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val URL = "https://haola.ru"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable fullscreen mode
        window.decorView.systemUiVisibility = (
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        // Create WebView programmatically
        webView = WebView(this)
        webView.layoutParams = android.view.ViewGroup.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContentView(webView)

        // Configure WebView
        setupWebView()

        // Load URL
        webView.loadUrl(URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val settings: WebSettings = webView.settings.apply {
            // Enable JavaScript
            javaScriptEnabled = true

            // Enable DOM Storage
            domStorageEnabled = true

            // Enable database
            databaseEnabled = true

            // Enable zoom controls
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false

            // Enable responsive design
            useWideViewPort = true
            loadWithOverviewMode = true

            // Improve performance
            cacheMode = WebSettings.LOAD_DEFAULT

            // Enable file access
            allowFileAccess = true
            allowContentAccess = true

            // Mixed content mode (for HTTP/HTTPS)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            // User agent
            userAgentString = "Mozilla/5.0 (Linux; Android ${android.os.Build.VERSION.RELEASE}; ${android.os.Build.MODEL}) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
        }

        // WebViewClient to handle page navigation
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    view?.loadUrl(it)
                }
                return true
            }
        }

        // Handle back button
        webView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.action == android.view.KeyEvent.ACTION_DOWN) {
                if (webView.canGoBack()) {
                    webView.goBack()
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}