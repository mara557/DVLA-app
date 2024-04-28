package com.mara.dvlavehicleinformation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var registrationNumberDvla: String
    private lateinit var registrationNumberMot: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the Submit button for DVLA Vehicle Information and set its click listener
        findViewById<View>(R.id.submitButtonDvla).setOnClickListener { onSubmitDvla() }

        // Find the AskMID button and set its click listener
        findViewById<View>(R.id.askMidButton).setOnClickListener { openAskMidWebPage() }

        // Find the Submit button for MOT History and set its click listener
        findViewById<View>(R.id.submitButtonMot).setOnClickListener { onSubmitMot() }
    }

    private fun onSubmitDvla() {
        registrationNumberDvla = findViewById<EditText>(R.id.editTextTextDvla).text.toString().trim()
        val errorMessageTextViewDvla = findViewById<TextView>(R.id.errorMessageTextViewDvla)

        if (registrationNumberDvla.isNotEmpty()) {
            errorMessageTextViewDvla.text = ""
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("registrationNumber", registrationNumberDvla)
            startActivity(intent)
        } else {
            errorMessageTextViewDvla.text = "Please enter a registration number."
        }
    }

    private fun openAskMidWebPage() {
        val intent = Intent(this, AskMidActivity::class.java)
        startActivity(intent)
    }

    private fun onSubmitMot() {
        registrationNumberMot = findViewById<EditText>(R.id.editTextTextMot).text.toString().trim()
        val errorMessageTextViewMot = findViewById<TextView>(R.id.errorMessageTextViewMot)

        if (registrationNumberMot.isNotEmpty()) {
            errorMessageTextViewMot.text = ""
            val intent = Intent(this, ResultsActivityMOT::class.java)
            val apiURL = "https://beta.check-mot.service.gov.uk/trade/vehicles/mot-tests?registration=$registrationNumberMot"
            intent.putExtra("apiURL", apiURL)
            startActivity(intent)
        } else {
            errorMessageTextViewMot.text = "Please enter a registration number."
        }
    }
}