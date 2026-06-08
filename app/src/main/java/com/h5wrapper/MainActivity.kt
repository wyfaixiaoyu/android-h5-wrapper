package com.h5wrapper

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val URL = "https://tapp.yuxiaor.com"
    private val LOCATION_PERMISSION_REQUEST = 1001

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set status bar and navigation bar colors to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = android.graphics.Color.WHITE
            window.navigationBarColor = android.graphics.Color.WHITE
        }

        // Enable fullscreen mode
        enableFullscreen()

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

    private fun enableFullscreen() {
        // Allow content to extend into display cutout area (for punch-hole cameras)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = 
                android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        // Hide navigation bar only, keep status bar visible
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )
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

            // Enable Geolocation
            setGeolocationEnabled(true)

            // User agent
            userAgentString = "Mozilla/5.0 (Linux; Android ${android.os.Build.VERSION.RELEASE}; ${android.os.Build.MODEL}) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
        }

        // WebChromeClient for geolocation
        webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                handleGeolocationPermission(origin, callback)
            }
        }
        
        // WebViewClient for page loading progress
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    view?.loadUrl(it)
                }
                return true
            }
            
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Page finished loading
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

    private fun handleGeolocationPermission(
        origin: String?,
        callback: GeolocationPermissions.Callback?
    ) {
        // Check if location permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            callback?.invoke(origin, true, false)
            return
        }

        // Store callback for later use in permission result
        pendingOrigin = origin
        pendingCallback = callback

        // Request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST
        )
    }

    private var pendingOrigin: String? = null
    private var pendingCallback: GeolocationPermissions.Callback? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pendingCallback?.invoke(pendingOrigin, true, false)
            } else {
                pendingCallback?.invoke(pendingOrigin, false, false)
            }
            pendingOrigin = null
            pendingCallback = null
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
