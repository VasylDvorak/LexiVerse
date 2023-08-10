package com.diplomproject.learningtogether.domain.repos

interface MeaningRepo {

    fun getImageUrl(word: String, onSuccess: (String?) -> Unit)
}