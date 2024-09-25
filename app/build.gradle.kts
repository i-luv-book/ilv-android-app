import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

// local.properties 파일 로드
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.sangik.iluvbook"
    compileSdk = 34

    buildFeatures{
        dataBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.sangik.iluvbook"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"${localProperties["BASE_URL"]}\"")
        }
        getByName("release") {
            buildConfigField("String", "BASE_URL", "\"${localProperties["BASE_URL"]}\"")
        }

        getByName("debug") {
            buildConfigField("String", "THUMBNAIL_TEST_IMG_URL", "\"${localProperties["THUMBNAIL_TEST_IMG_URL"]}\"")
        }
        getByName("release") {
            buildConfigField("String", "THUMBNAIL_TEST_IMG_URL", "\"${localProperties["THUMBNAIL_TEST_IMG_URL"]}\"")
        }

        getByName("debug") {
            buildConfigField("String", "FAIRYTALE_TEST_IMG_URL", "\"${localProperties["FAIRYTALE_TEST_IMG_URL"]}\"")
        }
        getByName("release") {
            buildConfigField("String", "FAIRYTALE_TEST_IMG_URL", "\"${localProperties["FAIRYTALE_TEST_IMG_URL"]}\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")

    // Flexbox
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // AndroidX Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")

    // AndroidX Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Testing
    testImplementation("junit:junit:4.13.2")

    // navigation-testing 의존성
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")

    // AndroidX Test - Unified Version for Testing Libraries
    val androidxTestVersion = "1.5.0"  // 호환되는 버전으로 변경
    val espressoVersion = "3.5.1" // 호환되는 버전으로 변경

    androidTestImplementation("org.mockito:mockito-android:5.4.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
    androidTestImplementation("androidx.test:core:$androidxTestVersion")
    androidTestImplementation("androidx.test:runner:$androidxTestVersion")
    androidTestImplementation("androidx.test:rules:$androidxTestVersion")

    implementation("me.relex:circleindicator:2.1.6")

    // Fragment testing
    debugImplementation("androidx.fragment:fragment-testing:1.8.2")

    // LiveData Testing
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")


    // ml kit 번역
    implementation("com.google.mlkit:translate:17.0.3")

}

