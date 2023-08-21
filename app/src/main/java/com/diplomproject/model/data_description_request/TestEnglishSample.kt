package com.diplomproject.model.data_description_request

import android.os.Parcelable
import com.diplomproject.model.data_word_request.DataModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class TestEnglishSample(
    var sampleDataModel: DataModel = DataModel(),
    var rightAnswer:Boolean?=false,
) : Parcelable