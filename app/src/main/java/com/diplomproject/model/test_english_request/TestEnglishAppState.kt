package com.diplomproject.model.test_english_request

import android.os.Parcelable
import com.diplomproject.model.data_description_request.Example
import kotlinx.android.parcel.Parcelize

@Parcelize
sealed class  TestEnglishAppState: Parcelable {
    @Parcelize
    data class Success(val data: List<Example>?) : TestEnglishAppState(), Parcelable

    @Parcelize
    data class Error(val error: Throwable) : TestEnglishAppState(), Parcelable

    @Parcelize
    data class Loading(val progress: Int?) : TestEnglishAppState(), Parcelable
}
