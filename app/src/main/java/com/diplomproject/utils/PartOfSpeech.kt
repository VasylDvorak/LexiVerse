package com.diplomproject.utils

import android.content.Context
import com.diplomproject.R
import org.koin.mp.KoinPlatform


private val contextApp by lazy { KoinPlatform.getKoin().get<Context>() }

enum class PartOfSpeech(val value: String) {
    n(contextApp.getString(R.string.noun)),
    v(contextApp.getString(R.string.verb)),
    j(contextApp.getString(R.string.adjective)),
    r(contextApp.getString(R.string.adverb)),
    prp(contextApp.getString(R.string.pretext)),
    prn(contextApp.getString(R.string.pronoun)),
    crd(contextApp.getString(R.string.cardinal_number)),
    cjc(contextApp.getString(R.string.union)),
    exc(contextApp.getString(R.string.interjection)),
    det(contextApp.getString(R.string.article)),
    abb(contextApp.getString(R.string.abbreviation)),
    x(contextApp.getString(R.string.particle)),
    ord(contextApp.getString(R.string.serial_number)),
    md(contextApp.getString(R.string.modal_verb)),
    ph(contextApp.getString(R.string.phrase)),
    phi(contextApp.getString(R.string.idiom))
}