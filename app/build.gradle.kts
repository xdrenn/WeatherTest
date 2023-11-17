plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.weathertest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weathertest"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures{
        viewBinding = true
    }

}

dependencies {

    val daggerVersion = "2.48.1"
    val retrofitVersion = "2.9.0"
    val okhttpVersion = "4.10.0"
    val picassoVersion = "2.8"
    val roomVersion = "2.6.0"
    val rxJavaVersion = "3.1.8"

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation( "com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    //dagger
    implementation ("com.google.dagger:dagger-android:$daggerVersion")
    implementation ("com.google.dagger:dagger-android-support:$daggerVersion")
    annotationProcessor("com.google.dagger:dagger-android-processor:$daggerVersion")
    annotationProcessor("com.google.dagger:dagger-compiler:$daggerVersion")

    // Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:$daggerVersion")
    annotationProcessor ("com.google.dagger:hilt-android-compiler:$daggerVersion")

    //picasso
    implementation("com.squareup.picasso:picasso:$picassoVersion")

    //room
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    //rxjava
    implementation ("io.reactivex.rxjava3:rxjava:$rxJavaVersion")
}