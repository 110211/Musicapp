plugins {
    id("com.android.application")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("../ks.jks")
            storePassword = "123456"
            keyAlias = "debug"
            keyPassword = "123456"
        }
    }
    android.buildFeatures.buildConfig=true
    namespace = "com.huce.hma"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.huce.hma"
        minSdk = 26
        targetSdk = 34
        versionCode = 9
        versionName = "1.8.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"http://10.0.2.2:3000\"")
        }
        release {
            buildConfigField("String", "API_URL", "\"http://10.8.0.254:3000\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {
    val lifecycle_version = "2.6.2"
    val room_version = "2.6.0"
    val lombok_version = "1.18.30"

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")

    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation ("com.google.android.material:material:1.10.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    //rxjava and rxandroid
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("androidx.room:room-rxjava2:2.6.0")

    compileOnly("org.projectlombok:lombok:$lombok_version")
    annotationProcessor("org.projectlombok:lombok:$lombok_version")

    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation ("com.facebook.android:facebook-android-sdk:8.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("se.emilsjolander:stickylistheaders:2.7.0")
    implementation ("com.squareup.picasso:picasso:2.71828")

}
