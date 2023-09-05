package com.diplomproject.view.base_for_dictionary

interface ViewLayout {

    fun responseEmpty() {}
    fun showViewLoading() {}
    fun showErrorScreen(error: String?) {}


}
