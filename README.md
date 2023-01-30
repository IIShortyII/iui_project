# iui_project
Project of WS22/23

This respository holds the Drunk-o-meter Android application. Together with the image processing and drunk prediction python script, it forms the Drunk-o-meter application.

## Camera and internet access

The Drunk-o-meter app requires camera permissions. In order to allow camera access, you have to allow them in the app settings first (long click on app icon, "App Info").
The app requires the device to have internet access to work.

## How to run the Drunk-o-meter application:

As this is a prototype, we only invested limited resources in the implementation of the communication between the Android app and the Drunk Face Prediction. Our solution requires you to manually add the ngrok link of the Flask Server as the POSTURL variable in the HomeActivity.java file (line 78) using Android Studio. Every time you start the server (from the drunk prediction python script), a new link is generated (free version, this sucks), which then has to be updated in the code manually.

Connect your Android device via USB to the laptop where you opened the Android project. Once you run the project in Android Studio, it is also executed on your phone. And then it's already time to analyze how drunk you are!
