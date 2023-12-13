package com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData

import javax.inject.Inject

class IsInternetConnected @Inject constructor(
    private val repository: DataRepository,
) {

    fun execute(): Boolean {
        return repository.isInternetConnected()
    }

}