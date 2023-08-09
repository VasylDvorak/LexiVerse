package com.diplomproject.learningtogether.domain.repos

interface MeaningRepo {

    suspend fun getImageUrl(word: String): String
}