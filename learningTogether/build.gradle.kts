plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.diplomproject.learningtogether"
}

dependencies {

    //Koin
    implementation(Koin.koin_android)

    //шрифты
    implementation("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:2.0.3")

    //picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    //MPAndroidChart Графики
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}