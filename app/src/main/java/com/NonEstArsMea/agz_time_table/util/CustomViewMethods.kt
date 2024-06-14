package com.NonEstArsMea.agz_time_table.util

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

fun getStaticLayout(
    text: String,
    width: Int,
    paint: TextPaint,
    alignLeft: Boolean = false
): StaticLayout {
    val align = if (alignLeft) {
        Layout.Alignment.ALIGN_NORMAL
    } else {
        Layout.Alignment.ALIGN_CENTER
    }
    return StaticLayout.Builder.obtain(
        /* source = */ text,
        /* start = */  0,
        /* end = */    text.length,
        /* paint = */  paint,
        /* width = */  width
    )
        .setAlignment(align)
        .setLineSpacing(0f, 1f)
        .setIncludePad(true)
        .build()
}