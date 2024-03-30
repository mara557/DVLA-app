DVLA Vehicle Information App

This Android application allows users to retrieve vehicle information from the DVLA (Driver and Vehicle Licensing Agency) database using the vehicle registration number. The app sends a request to the DVLA API and displays the retrieved information to the user.

Features
Input field to enter the vehicle registration number.
Submit button to initiate the API request.
Displays vehicle information retrieved from the DVLA database.
Error handling for invalid input and API responses.

Usage
Clone the Repository: Clone this repository to your local machine using the following command:

bash
Copy code
git clone <repository-url>
Import Project in Android Studio: Open Android Studio and import the cloned project by selecting the project's build.gradle file.

API Key Setup:

Obtain an API key from DVLA. This API key is required to access the DVLA API.
Place your API key in the config.properties file located in the res/raw directory. Ensure that the key is named api.key.
Build and Run: Build and run the project on an Android emulator or a physical device.

Enter Registration Number: In the main screen of the app, enter the vehicle registration number in the provided input field.

Submit: Tap on the "Submit" button to initiate the request to retrieve vehicle information.

View Results: Once the request is processed, the app will display the retrieved vehicle information on the screen.

Components
ApiKeyProvider: Provides methods to retrieve the API key required to access the DVLA API.
ApiRequestTask: AsyncTask subclass responsible for making the API request to the DVLA API. Executes the request in the background and returns the response to the main thread. Handles communication with the API including setting headers and parsing response.

MainActivity: Main activity of the app where users can enter the vehicle registration number. Validates input and navigates to the ResultsActivity upon submission.

ResultsActivity: Displays the retrieved vehicle information to the user. Handles API response, parses JSON, and dynamically creates TextViews to display information. Provides error handling for invalid responses.

Dependencies
This project does not rely on any third-party libraries or dependencies beyond the Android SDK.

Support and Updates
This application is a personal project developed for learning purposes and may or may not receive further updates.

Note: This app is for demonstration purposes only and is not affiliated with the DVLA. Use responsibly and ensure compliance with all relevant regulations and policies.
