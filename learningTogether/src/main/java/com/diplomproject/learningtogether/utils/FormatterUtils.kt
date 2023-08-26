package com.diplomproject.learningtogether.utils

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
var bpDataFormatter = SimpleDateFormat("dd.MM.yyyy")

@SuppressLint("DateFormat")
val decimalFormat = DecimalFormat("0.00")
