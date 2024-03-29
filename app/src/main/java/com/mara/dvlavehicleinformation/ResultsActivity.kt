package com.mara.dvlavehicleinformation

import ApiRequestTask
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.lang.System


class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)

        val registrationNumber = intent.getStringExtra("registrationNumber")

        // Access the API key from environment variables
        val apiKey = System.getenv("API_KEY") ?: ""

        // Execute AsyncTask to fetch vehicle information from API
        if (registrationNumber != null && apiKey.isNotEmpty()) {
            ApiRequestTask(registrationNumber, apiKey) { result ->
                handleApiResponse(result)
            }.execute()
        }
    }

    private fun handleApiResponse(result: String?) {
        if (!result.isNullOrEmpty()) { // Check if result is not null or empty
            try {
                val json = JSONObject(result)

                // Find TextViews in the layout and update them with JSON data
                val registrationNumberTextView = findViewById<TextView>(R.id.registrationNumberTextView)
                val taxStatusTextView = findViewById<TextView>(R.id.taxStatusTextView)
                val taxDueDateTextView = findViewById<TextView>(R.id.taxDueDateTextView)
                val motStatusTextView = findViewById<TextView>(R.id.motStatusTextView)
                val makeTextView = findViewById<TextView>(R.id.makeTextView)
                val yearOfManufactureTextView = findViewById<TextView>(R.id.yearOfManufactureTextView)
                val engineCapacityTextView = findViewById<TextView>(R.id.engineCapacityTextView)
                val co2EmissionsTextView = findViewById<TextView>(R.id.co2EmissionsTextView)
                val fuelTypeTextView = findViewById<TextView>(R.id.fuelTypeTextView)
                val markedForExportTextView = findViewById<TextView>(R.id.markedForExportTextView)
                val colourTextView = findViewById<TextView>(R.id.colourTextView)
                val typeApprovalTextView = findViewById<TextView>(R.id.typeApprovalTextView)
                val revenueWeightTextView = findViewById<TextView>(R.id.revenueWeightTextView)
                val euroStatusTextView = findViewById<TextView>(R.id.euroStatusTextView)
                val dateOfLastV5CIssuedTextView = findViewById<TextView>(R.id.dateOfLastV5CIssuedTextView)
                val wheelplanTextView = findViewById<TextView>(R.id.wheelplanTextView)
                val monthOfFirstRegistrationTextView = findViewById<TextView>(R.id.monthOfFirstRegistrationTextView)

                // Update TextViews with JSON data
                registrationNumberTextView.text = "Registration Number: ${json.getString("registrationNumber")}"
                taxStatusTextView.text = "Tax Status: ${json.getString("taxStatus")}"
                taxDueDateTextView.text = "Tax Due Date: ${json.getString("taxDueDate")}"
                motStatusTextView.text = "MOT Status: ${json.getString("motStatus")}"
                makeTextView.text = "Make: ${json.getString("make")}"
                yearOfManufactureTextView.text = "Year of Manufacture: ${json.getInt("yearOfManufacture")}"
                engineCapacityTextView.text = "Engine Capacity: ${json.getInt("engineCapacity")}"
                co2EmissionsTextView.text = "CO2 Emissions: ${json.getInt("co2Emissions")}"
                fuelTypeTextView.text = "Fuel Type: ${json.getString("fuelType")}"
                markedForExportTextView.text = "Marked for Export: ${json.getBoolean("markedForExport")}"
                colourTextView.text = "Colour: ${json.getString("colour")}"
                typeApprovalTextView.text = "Type Approval: ${json.getString("typeApproval")}"
                revenueWeightTextView.text = "Revenue Weight: ${json.getInt("revenueWeight")}"
                euroStatusTextView.text = "Euro Status: ${json.getString("euroStatus")}"
                dateOfLastV5CIssuedTextView.text = "Date of Last V5C Issued: ${json.getString("dateOfLastV5CIssued")}"
                wheelplanTextView.text = "Wheelplan: ${json.getString("wheelplan")}"
                monthOfFirstRegistrationTextView.text = "Month of First Registration: ${json.getString("monthOfFirstRegistration")}"

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            // Handle the case when result is empty or null
            // For example, display a message to the user or log a warning
            // In this case, let's set a default message to the TextViews
            val noDataText = "No data available"
            val registrationNumberTextView = findViewById<TextView>(R.id.registrationNumberTextView)
            val taxStatusTextView = findViewById<TextView>(R.id.taxStatusTextView)
            val taxDueDateTextView = findViewById<TextView>(R.id.taxDueDateTextView)
            val motStatusTextView = findViewById<TextView>(R.id.motStatusTextView)
            val makeTextView = findViewById<TextView>(R.id.makeTextView)
            val yearOfManufactureTextView = findViewById<TextView>(R.id.yearOfManufactureTextView)
            val engineCapacityTextView = findViewById<TextView>(R.id.engineCapacityTextView)
            val co2EmissionsTextView = findViewById<TextView>(R.id.co2EmissionsTextView)
            val fuelTypeTextView = findViewById<TextView>(R.id.fuelTypeTextView)
            val markedForExportTextView = findViewById<TextView>(R.id.markedForExportTextView)
            val colourTextView = findViewById<TextView>(R.id.colourTextView)
            val typeApprovalTextView = findViewById<TextView>(R.id.typeApprovalTextView)
            val revenueWeightTextView = findViewById<TextView>(R.id.revenueWeightTextView)
            val euroStatusTextView = findViewById<TextView>(R.id.euroStatusTextView)
            val dateOfLastV5CIssuedTextView = findViewById<TextView>(R.id.dateOfLastV5CIssuedTextView)
            val wheelplanTextView = findViewById<TextView>(R.id.wheelplanTextView)
            val monthOfFirstRegistrationTextView = findViewById<TextView>(R.id.monthOfFirstRegistrationTextView)

            registrationNumberTextView.text = "Registration Number: $noDataText"
            taxStatusTextView.text = "Tax Status: $noDataText"
            taxDueDateTextView.text = "Tax Due Date: $noDataText"
            motStatusTextView.text = "MOT Status: $noDataText"
            makeTextView.text = "Make: $noDataText"
            yearOfManufactureTextView.text = "Year of Manufacture: $noDataText"
            engineCapacityTextView.text = "Engine Capacity: $noDataText"
            co2EmissionsTextView.text = "CO2 Emissions: $noDataText"
            fuelTypeTextView.text = "Fuel Type: $noDataText"
            markedForExportTextView.text = "Marked for Export: $noDataText"
            colourTextView.text = "Colour: $noDataText"
            typeApprovalTextView.text = "Type Approval: $noDataText"
            revenueWeightTextView.text = "Revenue Weight: $noDataText"
            euroStatusTextView.text = "Euro Status: $noDataText"
            dateOfLastV5CIssuedTextView.text = "Date of Last V5C Issued: $noDataText"
            wheelplanTextView.text = "Wheelplan: $noDataText"
            monthOfFirstRegistrationTextView.text = "Month of First Registration: $noDataText"
        }
    }
}
