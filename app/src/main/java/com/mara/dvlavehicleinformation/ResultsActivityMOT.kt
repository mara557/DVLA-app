package com.mara.dvlavehicleinformation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException

class ResultsActivityMOT : AppCompatActivity() {

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorMessageTextView: TextView
    private lateinit var dynamicLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_mot)

        loadingProgressBar = findViewById(R.id.loadingProgressBarMOT)
        errorMessageTextView = findViewById(R.id.errorMessageTextViewMOT)
        dynamicLayout = findViewById(R.id.dynamicLayoutMOT)

        val apiURL = intent.getStringExtra("apiURL")
        Log.d("API_REQUEST", "API URL from Intent: $apiURL")

        apiURL?.let {
            fetchDataFromAPI(it)
        }
    }

    private fun fetchDataFromAPI(apiURL: String) {
        showLoading()

        val apiRequestTask = MOTApiRequestTask(this, apiURL) { response ->
            handleApiResponse(response)
        }
        apiRequestTask.execute()
    }

    private fun handleApiResponse(response: String) {
        hideLoading()

        try {
            val jsonArray = JSONArray(response)
            dynamicLayout.removeAllViews()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val registration = jsonObject.optString("registration")
                val registrationTextView = findViewById<TextView>(R.id.registrationTextView)
                registrationTextView.text = "Registration: $registration"

                val keys = arrayOf(
                    "make", "model", "firstUsedDate", "fuelType",
                    "primaryColour", "vehicleId", "registrationDate", "manufactureDate",
                    "engineSize"
                )

                for (key in keys) {
                    val value = jsonObject.optString(key)
                    addTextView("$key: $value", Color.BLACK)
                }
                addDivider()

                val motTestsArray = jsonObject.optJSONArray("motTests")
                motTestsArray?.let {
                    for (j in 0 until motTestsArray.length()) {
                        val motTestObject = motTestsArray.getJSONObject(j)
                        val completedDate = motTestObject.optString("completedDate")
                        val testResult = motTestObject.optString("testResult")
                        val testNumber = motTestObject.optString("motTestNumber")
                        val odometerValue = motTestObject.optString("odometerValue")
                        val odometerUnit = motTestObject.optString("odometerUnit")
                        val expiryDate = motTestObject.optString("expiryDate") // New field
                        val odometerResultType = motTestObject.optString("odometerResultType") // New field
                        val motTestDetails = "MOT Test Details:\n" +
                                "Completed Date: $completedDate\n" +
                                "Test Result: $testResult\n" +
                                "Test Number: $testNumber\n" +
                                "Expiry Date: $expiryDate\n" + // Include Expiry Date
                                "Odometer Value: $odometerValue $odometerUnit\n" + // Include Odometer Unit
                                "Odometer Result Type: $odometerResultType" // Include Odometer Result Type
                        addTextView(motTestDetails, Color.BLUE)

                        addDivider()

                        val rfrAndCommentsArray = motTestObject.optJSONArray("rfrAndComments")
                        rfrAndCommentsArray?.let {
                            for (k in 0 until rfrAndCommentsArray.length()) {
                                val rfrAndCommentsObject = rfrAndCommentsArray.getJSONObject(k)
                                val text = rfrAndCommentsObject.optString("text")
                                val type = rfrAndCommentsObject.optString("type")
                                val dangerous = rfrAndCommentsObject.optBoolean("dangerous")
                                val rfrAndCommentsDetails = "RFR and Comments:\n" +
                                        "Text: $text\n" +
                                        "Type: $type\n" +
                                        "Dangerous: $dangerous"
                                addTextView(rfrAndCommentsDetails, Color.RED)
                                addDivider()
                            }
                        }
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            showErrorMessage("Error parsing JSON: ${e.message}")
            Log.e("JSON_PARSE_ERROR", "Error parsing JSON: ${e.message}")
        }
    }


    private fun addTextView(text: String, color: Int) {
        val textView = TextView(this)
        textView.text = text
        textView.setTextColor(color)
        textView.textSize = 16f
        dynamicLayout.addView(textView)
    }

    private fun addDivider() {
        val divider = View(this)
        divider.setBackgroundColor(Color.GRAY)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            2
        )
        divider.layoutParams = params
        dynamicLayout.addView(divider)
    }

    private fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
        errorMessageTextView.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

    private fun showErrorMessage(message: String) {
        errorMessageTextView.text = message
        errorMessageTextView.visibility = View.VISIBLE
    }
}
