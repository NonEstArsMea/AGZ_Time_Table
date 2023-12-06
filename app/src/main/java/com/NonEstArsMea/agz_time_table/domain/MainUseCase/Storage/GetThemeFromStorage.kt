package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import javax.inject.Inject

class GetThemeFromStorage @Inject constructor(private val repository: StrotageRepository) {

    fun execute(): Int{
        return repository.getThemeFromStorage()
    }
}