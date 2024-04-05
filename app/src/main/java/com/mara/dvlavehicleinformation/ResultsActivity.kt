package com.mara.dvlavehicleinformation

import ApiRequestTask
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import android.graphics.Color
import android.view.Gravity

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val registrationNumber = intent.getStringExtra("registrationNumber")

        // Check if there's an error message passed from MainActivity
        val errorMessageFromMain = intent.getStringExtra("errorMessage")
        if (!errorMessageFromMain.isNullOrEmpty()) {
            // Display error message passed from MainActivity
            displayErrorMessage(errorMessageFromMain)
        } else {
            // Execute AsyncTask to fetch vehicle information from API
            if (registrationNumber != null) {
                ApiRequestTask(this, registrationNumber) { result ->
                    handleApiResponse(result)
                }.execute()
            } else {
                displayErrorMessage("Invalid registration number")
            }
        }
    }

    private fun handleApiResponse(result: String?) {
        if (!result.isNullOrEmpty()) {
            try {
                val json = JSONObject(result)

                if (json.has("errors")) {
                    val errorsArray = json.getJSONArray("errors")
                    if (errorsArray.length() > 0) {
                        val errorObject = errorsArray.getJSONObject(0)
                        val errorMessage = errorObject.getString("detail")
                        displayErrorMessage(errorMessage)
                        return
                    }
                }

                val layout = findViewById<LinearLayout>(R.id.dynamicLayout)

                // Loop through each key in the JSON object and create TextViews
                json.keys().forEach { key ->
                    val value = json.getString(key)

                    // Capitalize the first letter of the key
                    val capitalizedKey = key.capitalize()

                    // Create a LinearLayout to hold key and value TextViews
                    val linearLayout = LinearLayout(this)
                    linearLayout.orientation = LinearLayout.HORIZONTAL

                    // Create a TextView to display the key
                    val keyTextView = TextView(this)
                    keyTextView.text = capitalizedKey
                    keyTextView.textSize = 16f
                    keyTextView.setTextColor(Color.BLACK)
                    keyTextView.gravity = Gravity.START
                    keyTextView.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )

                    // Create a TextView to display the value
                    val valueTextView = TextView(this)
                    valueTextView.text = value
                    valueTextView.textSize = 16f
                    valueTextView.setTextColor(Color.BLACK)
                    valueTextView.gravity = Gravity.END
                    valueTextView.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )

                    // Add TextViews to the LinearLayout
                    linearLayout.addView(keyTextView)
                    linearLayout.addView(valueTextView)

                    // Add the LinearLayout to the layout
                    layout.addView(linearLayout)
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
