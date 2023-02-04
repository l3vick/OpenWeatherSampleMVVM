object Kotlin {
    const val kotlin_android = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
    const val std_lib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
}

object Android {
    const val applicationID = "es.pablorojas.openweathersamplemvvm"
    const val compileSdk = 33
    const val minSdkVersion = 26
    const val targetSdkVersion = 33
    const val versionCode = 1
    const val versionName = "1.0"
}

object Version {
    //Kotlin
    const val kotlin = "1.8.0"
    const val coroutines = "1.6.4"

    //Android Libs
    const val core_ktx = "1.9.0"
    const val app_compat = "1.6.0"
    const val material = "1.8.0"
    const val constraint_layout = "2.1.4"
    const val navigation = "2.5.3"
    const val lifecycle = "2.5.1"
    const val room = "2.5.0"
    const val data_store = "1.0.0"
    const val play_services = "21.0.1"

    //Third party Libs
    const val hilt = "2.44"
    const val retrofit = "2.9.0"
    const val logging_interceptor = "4.9.3"
    const val lottie = "5.2.0"

    //Test libs
    const val junit = "4.13.2"
    const val junit_ext = "1.1.5"
    const val espresso_core = "3.5.1"
}


object AndroidLibs {
    const val core_ktx = "androidx.core:core-ktx:${Version.core_ktx}"
    const val app_compat =  "androidx.appcompat:appcompat:${Version.app_compat}"
    const val material =  "com.google.android.material:material:${Version.material}"
    const val constraint_layout =  "androidx.constraintlayout:constraintlayout:${Version.constraint_layout}"
    const val navigation_fragment =  "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val navigation_ui =  "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    const val viewmodel =  "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val play_services = "com.google.android.gms:play-services-location:${Version.play_services}"
    const val room = "androidx.room:room-ktx:${Version.room}"
    const val data_store = "androidx.datastore:datastore-preferences:${Version.data_store}"
}

object Libs {
    const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hilt_compiler = "com.google.dagger:hilt-compiler:${Version.hilt}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val retrofit_gson_converter = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Version.logging_interceptor}"
    const val lottie = "com.airbnb.android:lottie:${Version.lottie}"
}

object TestLibs {
    const val junit = "junit:junit:${Version.junit}"
    const val junit_ext = "androidx.test.ext:junit:${Version.junit_ext}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Version.espresso_core}"
}