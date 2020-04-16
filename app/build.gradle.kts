import dependencies.Dep
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.oss.licenses.plugin")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("../keystore-local-release.jks")
            storePassword = "xxxxxx"
            keyAlias = "key0"
            keyPassword = "xxxxxx"
        }
    }
    compileSdkVersion(29)
    buildToolsVersion = "29.0.2"
    defaultConfig {
        applicationId = "jp.maskedronin.bitwatcher"
        minSdkVersion(23)
        targetSdkVersion(29)
        versionCode = 10000
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.schemaLocation" to "$projectDir/schemas".toString(),
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
        buildConfigField("String", "DB_NAME", "\"${findProperty("database_name")}\"")

        val dateTime = SimpleDateFormat("yyyyMMddHHmm").format(Date())
        setProperty("archivesBaseName", "${applicationId}_${versionCode}_${dateTime}")
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "\"[debug]\"")

            val dbPassphrase = "passphrase"
            buildConfigField("String", "DB_PASSPHRASE", "\"$dbPassphrase\"")
            resValue("string", "DB_PASSWORD_${findProperty("database_name")}", "\"$dbPassphrase\"")
        }
        getByName("release") {
            resValue("string", "app_name", "\"bitWatcher\"")

            buildConfigField("String", "DB_PASSPHRASE", "\"\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["release"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dataBinding {
        buildFeatures.dataBinding = true
    }

    testOptions {
        unitTests { options ->
            options.isIncludeAndroidResources = true
            options.isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    implementation(Dep.Kotlin.stdlib)
    implementation(Dep.KotlinX.serializationRuntime)

    // androidx
    implementation(Dep.AndroidX.appCompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.constraintLayout)
    implementation(Dep.AndroidX.swipeRefreshLayout)
    implementation(Dep.AndroidX.fragmentKtx)
    implementation(Dep.AndroidX.liveDataKtx)
    implementation(Dep.AndroidX.securityCrypto)
    implementation(Dep.AndroidX.room)
    kapt(Dep.AndroidX.roomCompiler)
    implementation(Dep.AndroidX.roomKtx)
    implementation(Dep.AndroidX.navigationFragmentKtx)
    implementation(Dep.AndroidX.navigationUiKtx)

    implementation(Dep.Dagger.dagger)
    kapt(Dep.Dagger.compiler)
    implementation(Dep.Dagger.android)
    kapt(Dep.Dagger.androidProcessor)
    implementation(Dep.Dagger.androidSupport)

    implementation(Dep.Retrofit.retrofit)
    implementation(Dep.Retrofit.kotlinxSerializationConverter)
    implementation(Dep.Retrofit.loggingInterceptor)

    implementation(Dep.sqlCipher)

    implementation(Dep.liveEvent)

    implementation(Dep.material)

    implementation(Dep.threeTenAbp)
    implementation(Dep.timber)

    implementation(Dep.ossLicense)

    debugImplementation(Dep.Hyperion.core)
    debugImplementation(Dep.Hyperion.attr)
    debugImplementation(Dep.Hyperion.buildConfig)
    debugImplementation(Dep.Hyperion.crash)
    debugImplementation(Dep.Hyperion.disk)
    debugImplementation(Dep.Hyperion.geigerCounter)
    debugImplementation(Dep.Hyperion.measurement)
    debugImplementation(Dep.Hyperion.phoenix)
    debugImplementation(Dep.Hyperion.recorder)
    debugImplementation(Dep.Hyperion.sharedPreferences)

    debugImplementation(Dep.debugDbEncrypt)

    debugImplementation(Dep.leakCanary)

    testImplementation(Dep.Test.junit)
    testImplementation(Dep.Test.core)
    // Kotlinx.serialization が Unit Testだと使えないため
    testImplementation(Dep.gson)

    androidTestImplementation(Dep.AndroidTest.runner)
    androidTestImplementation(Dep.AndroidTest.rules)
    androidTestImplementation(Dep.AndroidTest.junit)
    androidTestImplementation(Dep.AndroidTest.espressoCore)
}
