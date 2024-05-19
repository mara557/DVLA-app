package com.mara.dvlavehicleinformation.data.api

import android.content.Context
import com.mara.dvlavehicleinformation.R
import java.io.InputStream
import java.util.Properties

object ApiKeyProvider {
    private const val DVLA_API_KEY_PROPERTY = "api.key"
    private const val MOT_API_KEY_PROPERTY = "api.keyMOT"

    fun getApiKey(context: Context): String {
        return getApiKeyFromProperties(context, DVLA_API_KEY_PROPERTY)
    }

    fun getApiKeyMOT(context: Context): String {
        return getApiKeyFromProperties(context, MOT_API_KEY_PROPERTY)
    }

    private fun getApiKeyFromProperties(context: Context, key: String): String {
        val properties = Properties()
        val inputStream: InputStream = context.resources.openRawResource(R.raw.config)
        properties.load(inputStream)
        return properties.getProperty(key) ?: ""
    }
}