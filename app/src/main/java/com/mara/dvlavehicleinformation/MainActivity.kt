package com.mara.dvlavehicleinformation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the Submit button for DVLA Vehicle Information and set its click listener
        val submitButtonDvla = findViewById<View>(R.id.submitButtonDvla)
        submitButtonDvla.setOnClickListener {
            onSubmitDvla()
        }

        // Find the Submit button for MOT History and set its click listener
        val submitButtonMot = findViewById<View>(R.id.submitButtonMot)
        submitButtonMot.setOnClickListener {
            onSubmitMot()
        }
    }

    private fun onSubmitDvla() {
        val registrationNumberEditText = findViewById<EditText>(R.id.editTextTextDvla)
        val registrationNumber = registrationNumberEditText.text.toString().trim()

        // Find the error message TextView for DVLA Vehicle Information
        val errorMessageTextViewDvla = findViewById<TextView>(R.id.errorMessageTextViewDvla)

        // Check if the registration number is not empty
        if (registrationNumber.isNotEmpty()) {
            // Clear any previous error messages
            errorMessageTextViewDvla.text = ""

            // Navigate to the results activity and pass the registration number as an extra
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("registrationNumber", registrationNumber)
            startActivity(intent)
        } else {
            // Show an error message for empty input
            errorMessageTextViewDvla.text = "Please enter a registration number."
        }
    }

    private fun onSubmitMot() {
        val registrationNumberEditText = findViewById<EditText>(R.id.editTextTextMot)
        val registrationNumber = registrationNumberEditText.text.toString().trim()

        // Find the error message TextView for MOT History
        val errorMessageTextViewMot = findViewById<TextView>(R.id.errorMessageTextViewMot)

        // Check if the registration number is not empty
        if (registrationNumber.isNotEmpty()) {
            // Clear any previous error messages
            errorMessageTextViewMot.text = ""

            // Navigate to the results activity for MOT
            val intent = Intent(this, ResultsActivityMOT::class.java)
            val apiURL =
                "https://beta.check-mot.service.gov.uk/trade/vehicles/mot-tests?registration=$registrationNumber"
            intent.putExtra("apiURL", apiURL)
            startActivity(intent)
        } else {
            // Show an error message for empty input
            errorMessageTextViewMot.text = "Please enter a registration number."
        }
    }
}