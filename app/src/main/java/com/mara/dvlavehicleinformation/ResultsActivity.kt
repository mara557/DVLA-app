package com.mara.dvlavehicleinformation

import ApiRequestTask
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val registrationNumber = intent.getStringExtra("registrationNumber")

        if (!registrationNumber.isNullOrBlank()) {
            fetchVehicleInformation(registrationNumber)
        } else {
            displayErrorMessage("Invalid registration number")
        }
    }

    private fun fetchVehicleInformation(registrationNumber: String) {
        ApiRequestTask(this, registrationNumber) { result ->
            handleApiResponse(result)
        }.execute()
    }

    private fun handleApiResponse(result: String?) {
        if (result.isNullOrBlank()) {
            displayErrorMessage("Empty or null API response")
            return
        }

        try {
            val json = JSONObject(result)
            val errors = json.optJSONArray("errors")

            if (errors != null && errors.length() > 0) {
                val errorMessage = errors.getJSONObject(0).getString("detail")
                displayErrorMessage(errorMessage)
                return
            }

            val layout = findViewById<LinearLayout>(R.id.dynamicLayout)
            layout.removeAllViews() // Clear the layout before adding new views

            // Loop through each key in the JSON object and create TextViews
            json.keys().forEach { key ->
                val value = json.getString(key)

                // Capitalize the first letter of the key
                val capitalizedKey = key.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                }

                // Create a LinearLayout to hold key and value TextViews
                val linearLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(16) // Add padding for better readability
                }

                // Create a TextView to display the key
                val keyTextView = TextView(this).apply {
                    text = "$capitalizedKey:"
                    textSize = 16f
                    setTextColor(ContextCompat.getColor(this@ResultsActivity, R.color.colorBlack))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                // Create a TextView to display the value
                val valueTextView = TextView(this).apply {
                    text = value
                    textSize = 16f
                    setTextColor(ContextCompat.getColor(this@ResultsActivity, R.color.colorBlack))
                    gravity = Gravity.END
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                }

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
    }

    private fun displayErrorMessage(message: String) {
        val errorMessageTextView = findViewById<TextView>(R.id.errorMessageTextView)
        errorMessageTextView.text = message
    }
}