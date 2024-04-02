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

        // Find the Submit button and set its click listener
        val submitButton = findViewById<View>(R.id.submitButton)
        submitButton.setOnClickListener {
            onSubmit()
        }
    }

    private fun onSubmit() {
        val registrationNumberEditText = findViewById<EditText>(R.id.editTextText)
        val registrationNumber = registrationNumberEditText.text.toString().trim()

        // Find the error message TextView
        val errorMessageTextView = findViewById<TextView>(R.id.errorMessageTextView)

        // Check if the registration number is not empty
        if (registrationNumber.isNotEmpty()) {
            // Clear any previous error messages
            errorMessageTextView.text = ""

            // Navigate to the results activity and pass the registration number as an extra
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("registrationNumber", registrationNumber)
            startActivity(intent)
        } else {
            // Show an error message for empty input
            errorMessageTextView.text = "Please enter a registration number."
        }
    }
}
