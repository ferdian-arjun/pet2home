## Bangkit-Mobile-Development
The documentation of <b>Pet2Home</b> application in Bangkit Capstone Project 2022
![Slide 16_9 - 2](https://user-images.githubusercontent.com/63504249/173284212-870aa918-2234-463e-898f-da07685d10ed.png)

## About
Pet2Home is an application intended to raise awareness of abandoned animals, especially cats and dogs, and also to help potential adopters to find their pets. Aside from adopting animals, users also can use a Pet2Lens feature that can make it easier to find out the breeds of dogs or cats they found. This project expects to help improve the welfare of abandoned animals and reduce the level of animal violence to illegal trade in animals, especially dogs and cats.

This app is able to identify 5 breed of dog and 5 breed of cat and determine its quality with a Machine Learning model. And then it will insert the data into a database, which will save all the breed info which is saved into it. Currently the classifications are limited to:

## The Team

|            Member           | Student ID |        Path        |                    Role                    |                                                       Contacts                                                      |
| :-------------------------: | :--------: | :----------------: | :----------------------------------------: | :-----------------------------------------------------------------------------------------------------------------: |
|        Adam Aristama        | M2211F1958 |  Machine Learning  |         Machine Learning Engineer          |         [Github](https://github.com/Adamaristama)           |
|    Devany Putri Mirasati    | M7211F1957 |  Machine Learning  |          Machine Learning Engineer         |  [Github](https://github.com/devanyputri)  |
|     Dinda Fathihah Sari     | C7211F1960 |  Cloud Computing   |         DevOps Engineer                    |   [Github](https://github.com/dindafathihah)             |
|    Dimas Bayu Anjasmara     | C7266F2290 |  Cloud Computing   |         DevOps Engineer, Designer          |   [Github](https://github.com/dimas212bayu)    |
|   Ferdian Arjutama Narwan   | A2183G1774 |  Mobile Development|     Android Mobile Developer               |   [Github](https://github.com/ferddev21)            |
|      Indra Purnomo Aji      | A7299F2590 |  Mobile Development|    Android Mobile Developer                | [Github](https://github.com/innoji26) |

## Repositories

|   Learning Paths   |                                Link                                |
| :----------------: | :----------------------------------------------------------------: |
| Mobile Development | [Github](https://github.com/ferddev21/pet2home) |
|  Cloud Computing  | [Github](https://github.com/dindafathihah/cc-pet2home)  |
|   Machine Learning  | [Github](https://github.com/Adamaristama/bangkitpet2home)  |


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


## License
Distributed under the MIT License. See `LICENSE` for more information.

<p align="right"> Keep the Bangkit Spirit and Stay Safe! <br> C22-PS110 Teams </p>
