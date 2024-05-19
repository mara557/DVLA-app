package com.mara.dvlavehicleinformation.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mara.dvlavehicleinformation.R
import com.mara.dvlavehicleinformation.data.api.VehicleInformationApiTask
import com.mara.dvlavehicleinformation.data.api.ApiRequestTask
import org.json.JSONArray
import org.json.JSONException

class ResultsActivityMOT : AppCompatActivity() {

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorMessageTextView: TextView
    private lateinit var firstSectionLayout: LinearLayout
    private lateinit var secondSectionLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_mot)

        loadingProgressBar = findViewById(R.id.loadingProgressBarMOT)
        errorMessageTextView = findViewById(R.id.errorMessageTextViewMOT)
        firstSectionLayout = findViewById(R.id.firstSectionContentLayout)
        secondSectionLayout = findViewById(R.id.secondSectionContentLayout)

        val registrationNumber = intent.getStringExtra("registrationNumber")
        val apiURL = intent.getStringExtra("apiURL")
        val result = intent.getStringExtra("result")

        Log.d("API_REQUEST", "Registration Number: $registrationNumber")
        Log.d("API_REQUEST", "API URL from Intent: $apiURL")

        if (!apiURL.isNullOrEmpty()) {
            fetchDataFromAPI(apiURL)
        } else {
            showErrorMessage("Invalid API URL")
        }
    }

    private fun fetchDataFromAPI(apiURL: String) {
        showLoading()

        val apiRequestTask = VehicleInformationApiTask(this, apiURL) { response ->
            handleApiResponse(response)
        }
        apiRequestTask.execute()
    }

    private fun handleApiResponse(response: String) {
        hideLoading()

        try {
            val jsonArray = JSONArray(response)
            firstSectionLayout.removeAllViews()
            secondSectionLayout.removeAllViews()

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
                    addTextView(SpannableStringBuilder("$key: $value"), Color.BLACK, firstSectionLayout)
                }
                addDivider(firstSectionLayout)

                val motTestsArray = jsonObject.optJSONArray("motTests")
                motTestsArray?.let {
                    for (j in 0 until motTestsArray.length()) {
                        val motTestObject = motTestsArray.getJSONObject(j)
                        val completedDate = motTestObject.optString("completedDate")
                        val testResult = motTestObject.optString("testResult")
                        val testNumber = motTestObject.optString("motTestNumber")
                        val odometerValue = motTestObject.optString("odometerValue")
                        val odometerUnit = motTestObject.optString("odometerUnit")
                        val expiryDate = motTestObject.optString("expiryDate")
                        val odometerResultType = motTestObject.optString("odometerResultType")

                        val testResultColor = if (testResult == "FAILED") Color.RED else Color.GREEN
                        val testResultSpannable = SpannableString("Test Result: $testResult")
                        testResultSpannable.setSpan(
                            ForegroundColorSpan(testResultColor),
                            "Test Result: ".length,
                            testResultSpannable.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        val motTestDetails = SpannableStringBuilder()
                            .append("MOT Test Details:\n")
                            .append("Completed Date: $completedDate\n")
                            .append(testResultSpannable)
                            .append("\nTest Number: $testNumber\n")
                            .append("Expiry Date: $expiryDate\n")
                            .append("Odometer Value: $odometerValue $odometerUnit\n")
                            .append("Odometer Result Type: $odometerResultType")

                        addTextView(motTestDetails, Color.BLACK, secondSectionLayout)

                        addDivider(secondSectionLayout)

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
                                addTextView(SpannableStringBuilder(rfrAndCommentsDetails), Color.RED, secondSectionLayout)
                                addDivider(secondSectionLayout)
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

    private fun addTextView(text: SpannableStringBuilder, color: Int, layout: LinearLayout) {
        val textView = TextView(this)
        textView.text = text
        textView.setTextColor(color)
        textView.textSize = 16f
        layout.addView(textView)
    }

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