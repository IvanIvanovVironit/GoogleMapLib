# GoogleMapLib
## This library was created for:
+ determining own location
+ searching places on query
+ creating route between to places

## How use this Library
### Getting Started (Gradle / Android Studio)
#### 1. Add gradle dependency to your application.
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
   
#### 2. Add the dependency
```
dependencies {
    implementation 'com.github.IvanIvanovVironit:GoogleMapLib:0.3.4'
}
```      
   
#### 3. Next you must call class MapsUtil() in your Kotlin class and implement functions and pass parameters into it.
For example:
```kotlin
   MapsUtil().geoLocate(
        searchString,
        requreContext(),
        map
    )
```