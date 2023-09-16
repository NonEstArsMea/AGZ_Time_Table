package com.NonEstArsMea.agz_time_table.domain.useCases

import com.NonEstArsMea.agz_time_table.domain.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class GetLessonTimeTableUseCase(private val timeTableRepository: TimeTableRepository) {
    fun getLessonTimeTable(numberLesson: Int, dayOfWeek: Int): CellApi {
        return timeTableRepository.getLessonTimeTable(numberLesson, dayOfWeek)
    }
}