plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vyro.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vyro.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "2.1"

        // OAuth Client ID — injected from GOOGLE_OAUTH_CLIENT_ID env var at build time.
        // In GitHub Actions CI, this reads from the GitHub Secret.
        // For local builds without the env var, falls back to the placeholder.
        val oauthId = System.getenv("GOOGLE_OAUTH_CLIENT_ID") ?: "YOUR_GOOGLE_OAUTH_CLIENT_ID.apps.googleusercontent.com"
        buildConfigField("String", "OAUTH_CLIENT_ID", "\"$oauthId\"")
    }

    buildTypes {
        debug { isMinifyEnabled = false }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
    sourceSets {
        getByName("main") { kotlin.srcDirs("src/main/kotlin") }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Google Sign-In for YouTube auto-publish
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // HTTP client for YouTube Data API v3
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}
