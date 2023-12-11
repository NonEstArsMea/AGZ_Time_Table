package com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData

import android.content.Context
import javax.inject.Inject

class IsInternetConnected @Inject constructor(
    private val repository: DataRepository,
) {

    fun execute(): Boolean {
        return repository.isInternetConnected()
    }

}