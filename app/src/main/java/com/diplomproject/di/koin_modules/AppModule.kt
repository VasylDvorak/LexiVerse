package com.diplomproject.di.koin_modules


import android.content.Context


class AppModule() {
    fun applicationContext(context: Context) = context
}