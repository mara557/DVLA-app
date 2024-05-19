package com.mara.dvlavehicleinformation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mara.dvlavehicleinformation.R
import com.mara.dvlavehicleinformation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var registrationNumberDvla: String
    private lateinit var registrationNumberMot: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MainViewModel::class.java)

        // Find the Submit button for DVLA Vehicle Information and set its click listener
        findViewById<View>(R.id.submitButtonDvla).setOnClickListener { onSubmitDvla() }

        // Find the AskMID button and set its click listener
        findViewById<View>(R.id.askMidButton).setOnClickListener { openAskMidWebPage() }

        // Find the Submit button for MOT History and set its click listener
        findViewById<View>(R.id.submitButtonMot).setOnClickListener { onSubmitMot() }

        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.dvlaResult.observe(this) { result ->
            // Handle DVLA API result
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("registrationNumber", registrationNumberDvla)
            startActivity(intent)
        }

        mainViewModel.motResult.observe(this) { (registrationNumber, result) ->
            // Handle MOT API result
            val intent = Intent(this, ResultsActivityMOT::class.java)
            val apiURL = "https://beta.check-mot.service.gov.uk/trade/vehicles/mot-tests?registration=$registrationNumber"
            intent.putExtra("registrationNumber", registrationNumber)
            intent.putExtra("apiURL", apiURL)
            intent.putExtra("result", result)
            startActivity(intent)
        }
    }

    private fun onSubmitDvla() {
        registrationNumberDvla = findViewById<EditText>(R.id.editTextTextDvla).text.toString().trim()
        val errorMessageTextViewDvla = findViewById<TextView>(R.id.errorMessageTextViewDvla)

        if (registrationNumberDvla.isNotEmpty()) {
            errorMessageTextViewDvla.text = ""
            mainViewModel.fetchDvlaInformation(registrationNumberDvla)
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
            mainViewModel.fetchMotInformation(registrationNumberMot)
        } else {
            errorMessageTextViewMot.text = "Please enter a registration number."
        }
    }
}