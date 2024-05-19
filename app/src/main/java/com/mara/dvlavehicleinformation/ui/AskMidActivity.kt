package com.mara.dvlavehicleinformation.ui

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.mara.dvlavehicleinformation.R

class AskMidActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_mid)

        // Find the WebView
        val webView = findViewById<WebView>(R.id.webViewAskMid)

        // Enable JavaScript
        webView.settings.javaScriptEnabled = true

        // Set the WebView to open links within the WebView
        webView.settings.setSupportMultipleWindows(false)

        // Set the WebView to load links within the WebView
        webView.webViewClient = WebViewClient()

        // Enable viewport meta tag support
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

        // Adjust the initial scale and zoom controls
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.setSupportZoom(true)

        // Load the AskMID webpage with the new URL
        webView.loadUrl("https://ownvehicle.askmid.com/")

        // Log a message to verify if the activity is being created
        Log.d("AskMidActivity", "Activity created successfully")
    }

    private inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}