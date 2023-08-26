package com.diplomproject.view.base_fragment_dictionary

interface ViewLayout {

    fun responseEmpty() {}
    fun showViewLoading() {}
    fun showErrorScreen(error: String?) {}


}
