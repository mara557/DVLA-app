package com.mara.dvlavehicleinformation

import ApiKeyProvider
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MOTApiRequestTask(
    private val context: Context,
    private val apiURL: String, // Receive API URL directly
    private val callback: (String) -> Unit
) : AsyncTask<Void, Void, String>() {

    private lateinit var progressBar: ProgressBar
    private val apiKeyMOT: String

    init {
        // Get the API key from the ApiKeyProvider
        apiKeyMOT = ApiKeyProvider.getApiKeyMOT(context)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // Show loading indicator
        progressBar = (context as AppCompatActivity).findViewById(R.id.loadingProgressBarMOT) // Use correct ID here
        progressBar.visibility = View.VISIBLE
    }


    override fun doInBackground(vararg params: Void?): String {
        var response = ""
        var connection: HttpURLConnection? = null
        try {
            val url = URL(apiURL)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json+v6")
            connection.setRequestProperty("x-api-key", apiKeyMOT)

            // Log the headers
            Log.d("API_REQUEST", "Request Headers: ${connection.requestProperties}")

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val responseBuffer = StringBuffer()
                while (inputStream.readLine().also { line = it } != null) {
                    responseBuffer.append(line)
                }
                inputStream.close()
                response = responseBuffer.toString()
            } else {
                response = "Error: ${connection.responseMessage}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
        return response
    }

    override fun onPostExecute(result: String) {
        // Hide loading indicator
        progressBar.visibility = View.GONE

        Log.d("MOT_API_RESPONSE", result ?: "No response received")
        callback(result)
    }
}
