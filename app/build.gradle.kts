import java.text.SimpleDateFormat
import java.util.Date

val kotlinVersion: String by project
val navVersion: String by project

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

    // androidx
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.3.0-alpha03")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.security:security-crypto:1.0.0-beta01")
    val roomVersion = "2.2.5"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    val daggerVersion = "2.24"
    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")

    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.5.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.4.0")

    implementation("net.zetetic:android-database-sqlcipher:4.3.0")

    implementation("com.github.hadilq.liveevent:liveevent:1.2.0")

    implementation("com.google.android.material:material:1.1.0")

    implementation("com.jakewharton.threetenabp:threetenabp:1.2.2")
    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")

    val hyperionVersion = "0.9.27"
    debugImplementation("com.willowtreeapps.hyperion:hyperion-core:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-attr:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-build-config:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-crash:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-disk:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-geiger-counter:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-measurement:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-phoenix:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-recorder:$hyperionVersion")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-shared-preferences:$hyperionVersion")

    debugImplementation("com.amitshekhar.android:debug-db-encrypt:1.0.6")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.2")

    testImplementation("junit:junit:4.12")
    testImplementation("androidx.test:core:1.2.0")
    // Kotlinx.serialization が Unit Testだと使えないため
    testImplementation("com.google.code.gson:gson:2.8.6")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
