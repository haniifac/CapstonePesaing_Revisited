package com.haniifac.capstonepesaing_revisited.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var category: String,
    var img: Int,
) : Parcelable
