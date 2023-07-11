package com.diplomproject.model.repository


interface Repository<T> {

    suspend fun getData(word: String): T
    suspend fun getFavoriteList(): T
}
