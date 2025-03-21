package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.NetUtil
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.GetDataFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.SetDataInStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.NonEstArsMea.agz_time_table.util.DateManager
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для главной активности приложения.
 * Управляет состоянием темы, навигации, сети и данными, связанными с расписанием.
 */
class MainViewModel @Inject constructor(
    private val setDataInStorage: SetDataInStorageUseCase, // UseCase для сохранения данных
    private val getNameParam: GetMainParamUseCase, // UseCase для получения основного параметра
    private val getDataFromStorage: GetDataFromStorageUseCase, // UseCase для получения данных из хранилища
    private val timeTableRepositoryImpl: TimeTableRepository, // Репозиторий для работы с расписанием
    private val net: NetUtil, // Утилита для проверки сети
) : ViewModel() {

    // LiveData для хранения текущей темы приложения
    private var _theme = MutableLiveData<Int>()
    val theme: LiveData<Int>
        get() = _theme


    // LiveData для хранения выбранного элемента в нижнем меню
    private var _selectedItem: MutableLiveData<Int> = BottomMenuItemStateManager.getMenuItem()
    val selectedItem: LiveData<Int>
        get() = _selectedItem

    init {
        // Проверка состояния сети при инициализации ViewModel
        net.checkNetConn()
    }

    /**
     * Запускает приложение: загружает данные из хранилища и устанавливает тему.
     */
    fun startApp() {
        getDataFromStorage.execute() // Загрузка данных из хранилища
        _theme.value = getDataFromStorage.getTheme() // Установка текущей темы
    }

    /**
     * Возвращает основной параметр приложения.
     */
    fun getMainParam(): String {
        return getNameParam.getNameOfMainParamFromStorage()
    }

    /**
     * Сохраняет данные в хранилище.
     */
    fun setDataInStorage() {
        DateManager.setDayNow() // Устанавливает текущую дату
        viewModelScope.launch {
            setDataInStorage.execute(
                mainParam = getNameParam.getLiveData().value, // Основной параметр
                favMainParamList = timeTableRepositoryImpl.getArrayOfFavoriteMainParam().value, // Избранные параметры
                theme = _theme.value, // Текущая тема
                list = timeTableRepositoryImpl.getWeekTimeTable() // Расписание на неделю
            )
        }
    }

    /**
     * Проверяет, доступно ли сетевое соединение.
     */
    fun checkClickable(): Boolean {
        net.checkNetConn() // Проверка сети
        return net.isNetConnection() // Возвращает состояние сети
    }

    /**
     * Устанавливает новую тему приложения.
     * @param isChecked Флаг, указывающий, выбрана ли тема.
     * @param newTheme Идентификатор новой темы.
     */
    fun setTheme(isChecked: Boolean, newTheme: Int) {
        if (isChecked) {
            _theme.value = when (newTheme) {
                R.id.button1 -> MainActivity.SYSTEM_THEME // Системная тема
                R.id.button2 -> MainActivity.NIGHT_THEME // Темная тема
                R.id.button3 -> MainActivity.LIGHT_THEME // Светлая тема
                else -> MainActivity.SYSTEM_THEME // По умолчанию системная тема
            }
        }
    }

    /**
     * Возвращает идентификатор текущей темы.
     */
    fun checkTheme(): Int {
        return when (_theme.value) {
            MainActivity.SYSTEM_THEME -> R.id.button1 // Системная тема
            MainActivity.NIGHT_THEME -> R.id.button2 // Темная тема
            MainActivity.LIGHT_THEME -> R.id.button3 // Светлая тема
            else -> R.id.button1 // По умолчанию системная тема
        }
    }
}

