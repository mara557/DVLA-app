import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.mara.dvlavehicleinformation.R

class ApiRequestTask(
    private val context: Context,
    private val registrationNumber: String,
    private val callback: (String) -> Unit
) : AsyncTask<Void, Void, String>() {

    private lateinit var progressBar: ProgressBar

    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute()
        // Show loading indicator
        progressBar = (context as AppCompatActivity).findViewById(R.id.loadingProgressBar)
        progressBar.visibility = View.VISIBLE
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): String {
        val apiUrl = "https://driver-vehicle-licensing.api.gov.uk/vehicle-enquiry/v1/vehicles"
        val apiKey = ApiKeyProvider.getApiKey(context) // Access the API key
        val requestBody = "{\"registrationNumber\": \"$registrationNumber\"}" // JSON request body

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

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: String) {
        // Hide loading indicator
        progressBar.visibility = View.GONE

        Log.d("API_RESPONSE", result ?: "No response received")
        callback(result)
    }
}
