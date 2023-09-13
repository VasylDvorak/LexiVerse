import org.gradle.api.JavaVersion

object Config {
    const val application_id = "com.diplomproject"
    const val compile_sdk = 33
    const val min_sdk = 26
    const val target_sdk = 33
    val java_version = JavaVersion.VERSION_17
    const val jvmTarget = "17"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

}

object Releases {
    const val version_code = 1
    const val version_name = "1.0"
}

object Modules {
    const val app = ":app"
    const val learningTogether = ":learningTogether"
}

object Versions {
    //SplashScreen API
    const val splashscreen = "1.0.1"

    //Design
    const val appcompat = "1.6.1"
    const val material = "1.9.0"
    const val savedstate = "2.6.1"
    const val constraintlayout = "2.1.4"
    const val fragmentKtx = "2.5.3"
    const val uiKtx = "2.5.3" //
    const val swiperefreshlayout = "1.1.0"
    const val recyclerview = "1.3.0"
    const val viewmodelKtx = "1.6.1"
    const val ar_core = "1.37.0"

    //Kotlin
    const val core = "1.10.0"
    const val coroutinesCore = "1.7.0-RC"
    const val coroutinesAndroid = "1.7.0-RC"
    const val splashScreen = "1.0.1"

    //Retrofit
    const val retrofit = "2.9.0"
    const val converterGson = "2.9.0"
    const val interceptor = "4.9.2"
    const val adapterCoroutines = "0.9.2"

    //Koin
    const val koinAndroid = "3.4.0"
    const val koinCore = "3.4.0"

    const val koinCompat = "3.4.0"

    // Glide
    const val glide = "4.14.2"
    const val glideCompiler = "4.11.0"

    //Cicerone
    const val cicerone = "7.1"

    //Firebase
    const val bom = "30.3.1"
    const val auth = "22.1.0"
    const val database = "20.2.2"
    const val messagingKtx = "23.1.0"
    const val databaseKtx = "20.1.0"
    const val analyticsKtx = "21.2.0"
    const val authKtx = "21.1.0"

    //Room
    const val roomKtx = "2.5.1"
    const val runtime = "2.5.1"
    const val roomCompiler = "2.5.1"

    //Test
    const val jUnit = "4.13.2"
    const val junitImpl = "1.1.5"
    const val espressoCore = "3.5.1"
    const val testing = "2.2.0"

    //Mockito
    const val mockitoCore = "5.3.1"
    const val mockitoInline = "2.13.0"

    //Robolectric
    const val robolectric = "4.10.3"
    const val testCore = "1.5.0"
    const val testRunner = "1.5.2"
    const val testTruth = "1.5.0"
    const val testJunit = "1.1.5"
    const val testEspresso = "3.5.1"
    const val arCore = "1.39.0"

    //Automator
    const val uiautomator = "2.2.0"
    const val testRules = "1.6.0-alpha01"
    const val fragmentTesting = "1.6.1"

    //RecyclerView
    const val espressoContrib = "3.5.1"

    //CoroutineTests
    const val coroutinesTest = "1.4.3"

    //Fonts
    const val calligraphy3 = "3.1.1"
    const val viewpump = "2.0.3"

    //Picasso
    const val picasso = "2.71828"

    //MpAndroidChart
    const val mpAndroidChart = "v3.1.0"
}

object SplashScreen {
    const val core_splashscreen = "androidx.core:core-splashscreen:${Versions.splashscreen}"
}

object Design {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val savedstate =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.savedstate}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.fragmentKtx}"
    const val ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.uiKtx}"
    const val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodelKtx}"
    const val arCore = "com.google.ar:core:${Versions.ar_core}"

}

object Kotlin {
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    const val coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"
    const val splash_screen = "androidx.core:core-splashscreen:${Versions.splashScreen}"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.converterGson}"
    const val adapter_coroutines =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.adapterCoroutines}"
    const val logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
}

object Koin {
    const val koin_android = "io.insert-koin:koin-android:${Versions.koinAndroid}"
    const val koin_core = "io.insert-koin:koin-core:${Versions.koinCore}"
    const val koin_compat = "io.insert-koin:koin-android-compat:${Versions.koinCompat}"
}

object Cicerone {
    const val cicerone = "com.github.terrakok:cicerone:${Versions.cicerone}"
}

object Glide {
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glideCompiler}"
}

object Firebase {
    const val firebase_bom = "com.google.firebase:firebase-bom:${Versions.bom}"
    const val firebase_auth = "com.google.firebase:firebase-auth:${Versions.auth}"
    const val firebase_database = "com.google.firebase:firebase-database:${Versions.database}"
    const val crashlytics_ktx = "com.google.firebase:firebase-crashlytics-ktx"
    const val messaging_ktx_app =
        "com.google.firebase:firebase-messaging-ktx:${Versions.messagingKtx}"
    const val database_ktx_app =
        "com.google.firebase:firebase-database-ktx:${Versions.databaseKtx}"
    const val analytics_ktx_version =
        "com.google.firebase:firebase-analytics-ktx:${Versions.analyticsKtx}"
    const val firebase_auth_ktx = "com.google.firebase:firebase-auth-ktx:${Versions.authKtx}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.runtime}"
    const val annotation_processor_compiler =
        "androidx.room:room-compiler:${Versions.roomCompiler}"
    const val kapt_compiler = "androidx.room:room-compiler:${Versions.roomCompiler}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.roomKtx}"
}

object TestImpl {
    const val junit = "junit:junit:${Versions.jUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val test_imlement_junit = "androidx.test.ext:junit:${Versions.junitImpl}"
    const val core_testing = "androidx.arch.core:core-testing:${Versions.testing}"
}

object Mockito {
    const val mockito_core = "org.mockito:mockito-core:${Versions.mockitoCore}"
    const val mockito_inline = "org.mockito:mockito-inline:${Versions.mockitoInline}"
}

object Robolectric {
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val test_core = "androidx.test:core:${Versions.testCore}"
    const val test_runner = "androidx.test:runner:${Versions.testRunner}"
    const val test_truth = "androidx.test.ext:truth:${Versions.testTruth}"
    const val test_junit = "androidx.test.ext:junit:${Versions.testJunit}"
    const val test_espresso_core = "androidx.test.espresso:espresso-core:${Versions.testEspresso}"
    const val test_espresso_intents =
        "androidx.test.espresso:espresso-intents:${Versions.testEspresso}"
    const val ar_core = "com.google.ar:core:${Versions.arCore}"
}

object Automator {
    const val uiautomator = "androidx.test.uiautomator:uiautomator:${Versions.uiautomator}"
    const val test_rules = "androidx.test:rules:${Versions.testRules}"
    const val fragment_testing = "androidx.fragment:fragment-testing:${Versions.fragmentTesting}"
}

object RecyclerView {
    const val espresso_contrib =
        "androidx.test.espresso:espresso-contrib:${Versions.espressoContrib}"
}

object CoroutineTests {
    const val coroutines_test =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
}

object Fonts {
    const val calligraphy3 = "io.github.inflationx:calligraphy3:${Versions.calligraphy3}"
    const val viewpump = "io.github.inflationx:viewpump:${Versions.viewpump}"
}

object Picasso {
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
}

object MpAndroidChart {
    const val mp_android_chart = "com.github.PhilJay:MPAndroidChart:${Versions.mpAndroidChart}"
}
