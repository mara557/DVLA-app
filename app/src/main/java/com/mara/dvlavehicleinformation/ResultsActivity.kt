package com.mara.dvlavehicleinformation

import ApiRequestTask
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val registrationNumber = intent.getStringExtra("registrationNumber")

        // Execute AsyncTask to fetch vehicle information from API
        if (registrationNumber != null) {
            ApiRequestTask(this, registrationNumber) { result ->
                handleApiResponse(result)
            }.execute()
        } else {
            displayErrorMessage("Invalid registration number")
        }
    }

    private fun handleApiResponse(result: String?) {
        if (!result.isNullOrEmpty()) { // Check if result is not null or empty
            try {
                val json = JSONObject(result)

                // Check if the response contains an "errors" array
                if (json.has("errors")) {
                    val errorsArray = json.getJSONArray("errors")
                    if (errorsArray.length() > 0) {
                        // Handle the error by displaying the error detail
                        val errorObject = errorsArray.getJSONObject(0)
                        val errorMessage = errorObject.getString("detail")
                        displayErrorMessage(errorMessage)
                        return // Stop further processing
                    }
                }

                // Find the layout to dynamically add TextViews
                val layout = findViewById<LinearLayout>(R.id.dynamicLayout)

                // Loop through each key in the JSON object and create TextViews
                json.keys().forEach { key ->
                    val value = json.getString(key)

                    val textView = TextView(this)
                    textView.text = "$key: $value"
                    textView.textSize = 16f

                    layout.addView(textView)
                }

            } catch (e: JSONException) {
                displayErrorMessage("Registration number not found or INVALID")
                e.printStackTrace()
            }
        } else {
            displayErrorMessage("Empty or null API response")
        }
    }

    private fun displayErrorMessage(message: String) {
        val errorMessageTextView = findViewById<TextView>(R.id.errorMessageTextView)
        errorMessageTextView.text = message
    }
}