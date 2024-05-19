package com.mara.dvlavehicleinformation.data.api

import android.content.Context
import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class ApiRequestTask(
    private val context: Context,
    private val registrationNumber: String,
    private val callback: (String) -> Unit
) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String {
        val apiUrl = "https://driver-vehicle-licensing.api.gov.uk/vehicle-enquiry/v1/vehicles"
        val apiKey = ApiKeyProvider.getApiKey(context)
        val requestBody = "{\"registrationNumber\": \"$registrationNumber\"}"

        var response = ""
        var connection: HttpURLConnection? = null
        try {
            val url = URL(apiUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("x-api-key", apiKey)
            connection.doOutput = true

            val outputStream: OutputStream = connection.outputStream
            outputStream.write(requestBody.toByteArray())
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                val responseBuffer = StringBuffer()
                var line: String?
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
        callback(result)
    }
}