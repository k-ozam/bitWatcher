package dependencies

object Dep {
    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:4.0.0-beta04"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin}"
        const val safeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Version.navigation}"
        const val ossLicense = "com.google.gms:oss-licenses:0.9.2"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}"
    }

    object KotlinX {
        const val serializationRuntime =
            "org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val coreKtx = "androidx.core:core-ktx:1.2.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.0-alpha03"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
        const val securityCrypto = "androidx.security:security-crypto:1.0.0-beta01"
        private const val roomVersion = "2.2.5"
        const val room = "androidx.room:room-runtime:$roomVersion"
        const val roomCompiler = "androidx.room:room-compiler:$roomVersion" // kapt
        const val roomKtx = "androidx.room:room-ktx:$roomVersion"
        const val navigationFragmentKtx =
            "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
        const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    }

    object Dagger {
        private const val daggerVersion = "2.24"
        const val dagger = "com.google.dagger:dagger:$daggerVersion"
        const val compiler = "com.google.dagger:dagger-compiler:$daggerVersion" // kapt
        const val android = "com.google.dagger:dagger-android:$daggerVersion"
        const val androidProcessor =
            "com.google.dagger:dagger-android-processor:$daggerVersion" // kapt
        const val androidSupport = "com.google.dagger:dagger-android-support:$daggerVersion"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:2.7.2"
        const val kotlinxSerializationConverter =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.5.0"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.4.0"
    }

    const val sqlCipher = "net.zetetic:android-database-sqlcipher:4.3.0"
    const val liveEvent = "com.github.hadilq.liveevent:liveevent:1.2.0"
    const val material = "com.google.android.material:material:1.1.0"
    const val threeTenAbp = "com.jakewharton.threetenabp:threetenabp:1.2.2"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val ossLicense = "com.google.android.gms:play-services-oss-licenses:17.0.0"

    // debug
    object Hyperion {
        private const val hyperionVersion = "0.9.27"
        const val core = "com.willowtreeapps.hyperion:hyperion-core:$hyperionVersion"
        const val attr = "com.willowtreeapps.hyperion:hyperion-attr:$hyperionVersion"
        const val buildConfig =
            "com.willowtreeapps.hyperion:hyperion-build-config:$hyperionVersion"
        const val crash = "com.willowtreeapps.hyperion:hyperion-crash:$hyperionVersion"
        const val disk = "com.willowtreeapps.hyperion:hyperion-disk:$hyperionVersion"
        const val geigerCounter =
            "com.willowtreeapps.hyperion:hyperion-geiger-counter:$hyperionVersion"
        const val measurement =
            "com.willowtreeapps.hyperion:hyperion-measurement:$hyperionVersion"
        const val phoenix = "com.willowtreeapps.hyperion:hyperion-phoenix:$hyperionVersion"
        const val recorder = "com.willowtreeapps.hyperion:hyperion-recorder:$hyperionVersion"
        const val sharedPreferences =
            "com.willowtreeapps.hyperion:hyperion-shared-preferences:$hyperionVersion"
    }

    const val debugDbEncrypt = "com.amitshekhar.android:debug-db-encrypt:1.0.6"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.2"

    // test
    object Test {
        const val junit = "junit:junit:4.12"
        const val core = "androidx.test:core:1.2.0"
    }

    // Kotlinx.serialization が Unit Testだと使えないため
    const val gson = "com.google.code.gson:gson:2.8.6"

    // androidTest
    object AndroidTest {
        const val runner = "androidx.test:runner:1.2.0"
        const val rules = "androidx.test:rules:1.2.0"
        const val junit = "androidx.test.ext:junit:1.1.1"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
    }
}