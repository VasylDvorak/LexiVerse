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
buildscript {
    repositories {
        google()
        jcenter()
    }}
dependencies {

    //Fonts
    implementation(Fonts.calligraphy3)
    implementation(Fonts.viewpump)

    //Picasso
    implementation(Picasso.picasso)

    //MPAndroidChart Графики
    implementation(MpAndroidChart.mp_android_chart)
}