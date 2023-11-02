package com.checker

import android.graphics.Bitmap

data class AppItem(
    val id: Int,
    val bitmap: Bitmap,
    var text1: String,
    var text2: String?,
)