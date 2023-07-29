package com.diplomproject.model.data_word_request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
sealed class AppState : Parcelable {
    @Parcelize
    data class Success(val data: List<DataModel>?) : AppState(), Parcelable

    @Parcelize
    data class Error(val error: Throwable) : AppState(), Parcelable

    @Parcelize
    data class Loading(val progress: Int?) : AppState(), Parcelable
}
