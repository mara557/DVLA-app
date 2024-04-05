package com.mara.dvlavehicleinformation

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException

class ResultsActivityMOT : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_mot)

        // Retrieve the API URL from the intent extras
        val apiURL = intent.getStringExtra("apiURL")

        // Log the API URL received from intent
        Log.d("API_REQUEST", "API URL from Intent: $apiURL")

        // Make an API request for MOT history using the provided API URL
        val apiRequestTask = MOTApiRequestTask(this, apiURL!!) { response ->
            // Handle the API response here
            handleApiResponse(response)
        }
        apiRequestTask.execute()
    }

    private fun handleApiResponse(response: String) {
        // Parse JSON response
        try {
            val jsonArray = JSONArray(response)

            // Find the LinearLayout in the layout
            val dynamicLayout = findViewById<LinearLayout>(R.id.dynamicLayoutMOT)

            // Clear any previous views
            dynamicLayout.removeAllViews()

            // Iterate over each item in the JSON array
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                // Create a ScrollView for each JSON object
                val scrollView = ScrollView(this)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                scrollView.layoutParams = layoutParams

                // Create a LinearLayout for each JSON object
                val objectLayout = LinearLayout(this)
                objectLayout.orientation = LinearLayout.VERTICAL
                val objectLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                objectLayout.layoutParams = objectLayoutParams

                // Iterate over each key-value pair in the JSON object
                val keys = arrayOf(
                    "registration", "make", "model", "firstUsedDate", "fuelType",
                    "primaryColour", "vehicleId", "registrationDate", "manufactureDate",
                    "engineSize"
                )
                for (key in keys) {
                    val value = jsonObject.optString(key)

                    // Create a TextView for each key-value pair
                    val textView = TextView(this)
                    textView.text = "$key: $value"
                    textView.setTextColor(Color.BLACK)
                    textView.textSize = 16f

                    // Add the TextView to the LinearLayout for the object
                    objectLayout.addView(textView)
                }

                // Add a divider between sections
                addDivider(objectLayout)

                // Iterate over motTests array
                val motTestsArray = jsonObject.optJSONArray("motTests")
                motTestsArray?.let {
                    for (j in 0 until motTestsArray.length()) {
                        val motTestObject = motTestsArray.getJSONObject(j)
                        val completedDate = motTestObject.optString("completedDate")
                        val testResult = motTestObject.optString("testResult")
                        val testNumber = motTestObject.optString("motTestNumber")
                        val odometerValue = motTestObject.optString("odometerValue")
                        val odometerUnit = motTestObject.optString("odometerUnit")

                        // Create a TextView for motTest details
                        val motTestTextView = TextView(this)
                        motTestTextView.text = "MOT Test Details:\n" +
                                "Completed Date: $completedDate\n" +
                                "Test Result: $testResult\n" +
                                "Test Number: $testNumber\n" +
                                "Odometer Value: $odometerValue $odometerUnit"
                        motTestTextView.setTextColor(Color.BLUE)
                        motTestTextView.textSize = 16f

                        // Add the TextView to the LinearLayout for the object
                        objectLayout.addView(motTestTextView)

                        // Add a divider between sections
                        addDivider(objectLayout)

                        // Iterate over rfrAndComments array
                        val rfrAndCommentsArray = motTestObject.optJSONArray("rfrAndComments")
                        rfrAndCommentsArray?.let {
                            for (k in 0 until rfrAndCommentsArray.length()) {
                                val rfrAndCommentsObject = rfrAndCommentsArray.getJSONObject(k)
                                val text = rfrAndCommentsObject.optString("text")
                                val type = rfrAndCommentsObject.optString("type")
                                val dangerous = rfrAndCommentsObject.optBoolean("dangerous")

                                // Create a TextView for rfrAndComments details
                                val rfrAndCommentsTextView = TextView(this)
                                rfrAndCommentsTextView.text = "RFR and Comments:\n" +
                                        "Text: $text\n" +
                                        "Type: $type\n" +
                                        "Dangerous: $dangerous"
                                rfrAndCommentsTextView.setTextColor(Color.RED)
                                rfrAndCommentsTextView.textSize = 16f

                                // Add the TextView to the LinearLayout for the object
                                objectLayout.addView(rfrAndCommentsTextView)

                                // Add a divider between sections
                                addDivider(objectLayout)
                            }
                        }
                    }
                }

                // Add the LinearLayout for the object to the ScrollView
                scrollView.addView(objectLayout)

                // Add the ScrollView to the main layout
                dynamicLayout.addView(scrollView)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            // Handle JSON parsing error
            Log.e("JSON_PARSE_ERROR", "Error parsing JSON: ${e.message}")
        }
    }

    // Function to add a divider view to the LinearLayout
    private fun addDivider(layout: LinearLayout) {
        val divider = View(this)
        divider.setBackgroundColor(Color.GRAY)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            2
        )
        divider.layoutParams = params
        layout.addView(divider)
    }
}
