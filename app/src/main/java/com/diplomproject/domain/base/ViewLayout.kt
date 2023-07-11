package com.diplomproject.domain.base

interface ViewLayout {

    fun responseEmpty() {}

    fun showViewLoading() {}

    fun showErrorScreen(error: String?) {}


}
