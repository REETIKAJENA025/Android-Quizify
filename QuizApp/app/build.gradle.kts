plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.quizapp"
    compileSdk = 36   // ✅ Updated to latest stable version

    defaultConfig {
        applicationId = "com.example.quizapp"
        minSdk = 25
        targetSdk = 36   // ✅ Match compileSdk
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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // ✅ Ensure dependency compatibility
    configurations.all {
        resolutionStrategy {
            // Forces consistent library versions
            force("nl.dionsegijn:konfetti-xml:2.0.2")
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 🎉 Confetti animation library
    implementation("nl.dionsegijn:konfetti-xml:2.0.2")
}
