package com.h5wrapper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var sharedPreferences: SharedPreferences
    private var currentUrl: String = ""

    // Triple tap detection
    private var tapCount = 0
    private var lastTapTime = 0L
    private val TAP_TIMEOUT = 500 // 500ms timeout between taps
    private val REQUIRED_TAPS = 3

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences for saving URL
        sharedPreferences = getSharedPreferences("H5WrapperPrefs", Context.MODE_PRIVATE)

        // Load saved URL or use default
        currentUrl = sharedPreferences.getString("saved_url", "https://haola.ru") ?: "https://haola.ru"

        // Create WebView programmatically
        webView = WebView(this)
        setContentView(webView)

        // Configure WebView
        setupWebView()

        // Setup triple tap listener
        setupTripleTapListener()

        // Load URL
        webView.loadUrl(currentUrl)
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

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Show current URL as toast when page loads
                url?.let {
                    if (it != currentUrl) {
                        currentUrl = it
                        Toast.makeText(this@MainActivity, "当前地址: $it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupTripleTapListener() {
        webView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentTime = System.currentTimeMillis()

                if (currentTime - lastTapTime < TAP_TIMEOUT) {
                    tapCount++
                } else {
                    tapCount = 1
                }

                lastTapTime = currentTime

                if (tapCount == REQUIRED_TAPS) {
                    tapCount = 0
                    showUrlInputDialog()
                    return true
                }
            }
            false
        }

        // Handle back button
        webView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.action == android.view.KeyEvent.ACTION_DOWN) {
                if (webView.canGoBack()) {
                    webView.goBack()
                    return@setOnKeyListener true
                }
            }
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showUrlInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("切换域名")

        // Create input field
        val input = EditText(this)
        input.setText(currentUrl)
        input.hint = "例如: tapp.yuxiaor.com"
        builder.setView(input)

        // Set up buttons
        builder.setPositiveButton("确定") { _, _ ->
            val newUrl = input.text.toString().trim()
            if (newUrl.isNotEmpty()) {
                // Add https:// prefix if not present
                var finalUrl = newUrl
                if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
                    finalUrl = "https://$finalUrl"
                }

                // Save URL to SharedPreferences
                sharedPreferences.edit().putString("saved_url", finalUrl).apply()

                // Update current URL and load
                currentUrl = finalUrl
                webView.loadUrl(finalUrl)

                Toast.makeText(this, "切换到: $finalUrl", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("取消") { dialog, _ ->
            dialog.cancel()
        }

        // Add quick buttons for common URLs
        builder.setNeutralButton("预设") { _, _ ->
            showQuickUrlDialog()
        }

        builder.show()
    }

    private fun showQuickUrlDialog() {
        val presetUrls = arrayOf(
            "https://haola.ru",
            "https://tapp.yuxiaor.com",
            "https://example.com"
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("选择预设域名")
        builder.setItems(presetUrls) { _, which ->
            val selectedUrl = presetUrls[which]

            // Save URL to SharedPreferences
            sharedPreferences.edit().putString("saved_url", selectedUrl).apply()

            // Update current URL and load
            currentUrl = selectedUrl
            webView.loadUrl(selectedUrl)

            Toast.makeText(this, "切换到: $selectedUrl", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}