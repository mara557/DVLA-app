package com.mara.dvlavehicleinformation.data.api

import android.content.Context
import android.os.AsyncTask
import okhttp3.*
import java.io.IOException

class VehicleInformationApiTask(
    private val context: Context,
    private val apiUrl: String,
    private val callback: (String) -> Unit
) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .header("Accept", "application/json+v6")
            .header("x-api-key", ApiKeyProvider.getApiKeyMOT(context))
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body()?.string() ?: ""
            } else {
                "Error: ${response.message()}"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    override fun onPostExecute(result: String) {
        callback(result)
    }
}