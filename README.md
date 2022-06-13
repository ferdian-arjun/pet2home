# Bangkit-Mobile-Development

## Mobile Development Documentation
The source code of Android app of Travens using Kotlin in order to complete Bangkit Capstone Project.



 - ### Feature
      * **Splash Screen**, There is logo screen before into the login page

      * **Login**, Allows a user to gain access to an application by entering their username and password

      * **SignUp**, Enables users to independently register and gain access to the system

      * **Home**, The start page that is displayed when you have logged in on your device
      
      *  **Profile**, page to view user profiles and display user posts
 
      * **PetLens**, You can take image from camera in preparation for uploading an image to detect Race of animals.

      * **Send image to server to detect race of animals**, After you prepare the image, you can click the process button to send the image and detect the image and get the detail about the race.


* #### Dependencies :
  - [Jetpack Compose](https://developer.android.com/jetpack/compose)
  - [Lifecycle & Livedata](https://developer.android.com/jetpack/androidx/releases/lifecycle)
  - [Navigation Component](https://developer.android.com/jetpack/androidx/releases/navigation)
  - [kotlinx-coroutines](https://developer.android.com/kotlin/coroutines)    
  - [Retrofit 2](https://square.github.io/retrofit/)   
  - [StickyScrollView Library](https://github.com/amarjain07/StickyScrollView)    
  - [Ok Http 3](https://square.github.io/okhttp/) 
  - [Place API](https://developers.google.com/maps/documentation/places/android-sdk) 

### Getting Started Application

  - ### Prerequisites
      - ##### Tools Sofware
        - [Android Studio](https://developer.android.com/studio)
        - JRE (Java Runtime Environment) or JDK (Java Development Kit).

      - #### Installation
        - Get an API Key at [Google Maps Platform](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
        - Clone this repository and import into Android Studio    
            ```
               https://github.com/ferddev21/pet2home.git
            ``` 
        - Enter your API in buildConfigField `build.graddle`
           ``` defaultConfig {
               buildConfigField("String", "MAPS_API_KEY", '"Your Api Key"')}
  ## Acknowledgements
  * [Clean Architecture Guide](https://developer.android.com/jetpack/guide)
  * [Android Application Fundamental](https://developer.android.com/guide/components/fundamentals)
  * [Android Jetpack Pro](https://developer.android.com/jetpack)
  * [Dependency injection](https://developer.android.com/training/dependency-injection)
