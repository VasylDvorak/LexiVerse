package com.diplomproject.di.koin_modules


import android.content.Context


class AppModule() {
    fun applicationContext(context: Context) = context
    fun getSecondArray(multiply: Int):Array<Int>{
        val numbers: Array<Int> = arrayOf(1, 2, 3, 4, 5)
        for(i in numbers.indices){
            numbers[i]*=multiply*2
        }
        return numbers
    }
    fun getFirstArray(multiply: Int): Array<Int> {
        val numbers: Array<Int> = arrayOf(1, 2, 3, 4, 5)
        for(i in numbers.indices){
            numbers[i]*=multiply
        }
        return numbers
    }
}