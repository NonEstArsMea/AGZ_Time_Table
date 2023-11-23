package com.NonEstArsMea.agz_time_table.domain.dataClass

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.unit.dp
import com.NonEstArsMea.agz_time_table.R

data class CellApi(
    var subject: String? = null,
    var teacher: String?= null,
    var classroom: String?= null,
    var studyGroup: String?= null,
    var date: String?= null,
    var subjectType: String?= null,
    var startTime: String?= null,
    var endTime: String?= null,
    var subjectNumber: Int?= null,
    var noEmpty: Boolean,
    var text: String?= null,
    var lessonTheme: String? = null,
    var color: Int= R.color.yellow_fo_lessons_card,
    val viewType: Int?= null,
    val viewSize: Int?= null,
    var isGone: Boolean = true
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subject)
        parcel.writeString(teacher)
        parcel.writeString(classroom)
        parcel.writeString(studyGroup)
        parcel.writeString(date)
        parcel.writeString(subjectType)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeValue(subjectNumber)
        parcel.writeByte(if (noEmpty) 1 else 0)
        parcel.writeString(text)
        parcel.writeString(lessonTheme)
        parcel.writeInt(color)
        parcel.writeValue(viewType)
        parcel.writeValue(viewSize)
        parcel.writeByte(if (isGone) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CellApi> {
        override fun createFromParcel(parcel: Parcel): CellApi {
            return CellApi(parcel)
        }

        override fun newArray(size: Int): Array<CellApi?> {
            return arrayOfNulls(size)
        }
    }

}

sealed class Cell
data class LessonTimeTable(
    var subject: String?,
    var teacher: String?,
    var classroom: String?,
    var studyGroup: String?,
    var date: String?,
    var subjectType: String?,
    var startTime: String?,
    var endTime: String?,
    var subjectNumber: Int?,
    var noEmpty: Boolean = true,
    var lessonTheme: String? = null,
    var color: Int = R.color.yellow_fo_lessons_card,
    val viewType: Int = 0,
    val isVisible: Boolean = false
):Cell()
data class BreakCell(
    var text: String?,
    var noEmpty: Boolean = true,
    val viewType: Int = 1,
    val viewSize: Int = 20,
):Cell()