package com.diplomproject.learningtogether.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

/**
 * Специально для форматирования значений. Форматируется дата и время.
 */

@SuppressLint("SimpleDateFormat")
var bpTimeFormatter = SimpleDateFormat("HH:mm")

@SuppressLint("SimpleDateFormat")
var bpDataFormatter = SimpleDateFormat("dd.MM.yyyy")

@SuppressLint("SimpleDateFormat")
var bpDataTimeFormatter = SimpleDateFormat("dd.MM.yyyy   HH:mm")