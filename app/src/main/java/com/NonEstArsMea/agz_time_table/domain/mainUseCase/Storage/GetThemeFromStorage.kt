package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import javax.inject.Inject

class GetThemeFromStorage @Inject constructor(private val repository: StorageRepository) {

    fun execute(): Int{
        return repository.getThemeFromStorage()
    }
}