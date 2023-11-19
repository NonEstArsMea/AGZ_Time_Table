package com.NonEstArsMea.agz_time_table.present

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.LoadDataUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetFavoriteMainParamsFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetLastWeekFromeStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetMainParamFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.SetDataInStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetListOfMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetWeekTimeTableListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(
    private val getMainParamFromStorage : GetMainParamFromStorageUseCase,
    private val getFavoriteMainParamsFromStorage: GetFavoriteMainParamsFromStorageUseCase,
    private val getLastWeekTimeTableFromStorage: GetLastWeekFromeStorageUseCase,
    private val setDataInStorage: SetDataInStorageUseCase,
    private val loadData: LoadDataUseCase
): ViewModel() {
    // хранит список с расписанием
    private val _dataLiveData = MutableLiveData<String>()
    val dataLiveData: LiveData<String>
        get() = _dataLiveData

    // хранит Calendar
    private val _calendarLiveData = MutableLiveData<Calendar>()
    val calendarLiveData: LiveData<Calendar>
        get() = _calendarLiveData

    // хранит массив с расписанием на неделю
    private val _weekTimeTable = MutableLiveData<ArrayList<ArrayList<CellApi>>>()
    val weekTimeTable: LiveData<ArrayList<ArrayList<CellApi>>>
        get() = _weekTimeTable

    // хранит состояние загрузки
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    // хранит список с главными параметрами
    private val _listOfMainParam = MutableLiveData<List<MainParam>>()
    val listOfMainParam: LiveData<List<MainParam>>
        get() = _listOfMainParam

    // Хранит параметр поиска
    private val _mainParam = MutableLiveData<String>()
    val mainParam: LiveData<String>
        get() = _mainParam

    // Хранит Избранные контакты
    private val _arrFavoriteMainParam = MutableLiveData<ArrayList<MainParam>>()
    val arrFavoriteMainParam: LiveData<ArrayList<MainParam>>
        get() = _arrFavoriteMainParam

    private var jobVM = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + jobVM)

    private var job: Job = uiScope.launch {  }

    private val repository = TimeTableRepositoryImpl

    private val getWeekTimeTableUseCase = GetWeekTimeTableListUseCase(repository)

    private val getListOfMainParamUseCase = GetListOfMainParamUseCase(repository)


    // закгрузка данных и сохраниение
    fun loadDataFromURL(){
        uiScope.launch {
            try {
                val data = async(Dispatchers.IO) {
                    loadData.execute()
                }
                Log.e("my-tag", data.await().toString())
                _dataLiveData.value = data.await().value
            } catch (e: Exception) {
                Log.e("my-tag", e.toString())
            }
        }
    }


    // создание массивов с расписанием
    fun getTimeTable(newData: String, dayOfWeek: ArrayList<String>, mainParam: String){
        // для прерывания предыдущих корутин
        if (job.isActive) {
            job.cancel()
        }
        job = uiScope.launch {
            try {
                setConditionLoading(true)
                _weekTimeTable.value = getWeekTimeTableUseCase.execute(newData, dayOfWeek, mainParam)
                setConditionLoading(false)
            } catch (e: Exception) {
                Log.e("Flow exception", e.toString())
            }
        }
        uiScope.launch {
            getListOfMainParam(newData)
        }
    }

    // получение списка с параметром поиска
    fun getListOfMainParam(data: String){
            _listOfMainParam.value = getListOfMainParamUseCase.execute(data)
    }

    // установка параметра выбора
    fun setMainParam(mainParam: String){
        _mainParam.value = mainParam
    }

    // получение главного параметра
    fun getMainParam(): String{
        return if(_mainParam.value != null) {
            _mainParam.value.toString()
        }else{
            "Группа не выбрана"
        }

    }
    // Установка нового календаря для смены даты по параметрам
    fun setNewCalendar(year: Int, month: Int, dayOfMonth: Int){
        _calendarLiveData.value?.set(year, month, dayOfMonth)
    }

    // Получение календаря
    fun getCalendar():Calendar{
        return _calendarLiveData.value!!
    }
    // Установка календаря без параметров
    fun setCalendar(calendar: Calendar){
        _calendarLiveData.value = calendar
    }
    // Остановка корутины после завершения работы
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


    // Получение списка ГЛАВНЫХ ПАРМЕТРОВ
    fun getListOfFavoriteMainParam(): ArrayList<String> {
            val arr: ArrayList<String> = arrayListOf()
            for(a in _arrFavoriteMainParam.value!!){
                arr.add(a.name)
            }
            return arr
    }
    // Изменение списка с избранными параметрами
    // Необходимо доделать момент, когда выбранная группа уже есть
    fun updateListOfFavoriteMainParam(param: MainParam){
        with(_arrFavoriteMainParam) {
            // Если не инициализирован список
            if (this.value == null) {
                this.value = arrayListOf()
            }

            if (param !in this.value!!) {
                this.value!!.add(0, param)
            }else{
                val index = this.value!!.indexOf(param)
                val item = this.value!![index]
                this.value!![index] = this.value!![0]
                this.value!![0] = item
            }
            // Если количество максимально
            if (this.value!!.size == 6) {
                this.value!!.removeLast()
            }
        }
    }

    fun moveItemInFavoriteMainParam(param: MainParam){
        val list = _arrFavoriteMainParam.value!!.toList() as ArrayList
        with(list) {
            if (this.size != 1) {
                val itemIndex = this.indexOf(param)
                for (item in itemIndex downTo 1) {
                    this[item] = this[item-1]
                }
                this[0] = param
            }
            _arrFavoriteMainParam.value = list
        }
    }


    fun delParamFromFavoriteMainParam(index: MainParam){
        Log.e("index", index.toString())
        val items = _arrFavoriteMainParam.value!!
        items.removeAt(_arrFavoriteMainParam.value!!.indexOf(index))
        _arrFavoriteMainParam.value = items
    }
/**
       Установка списка параметров после загрузки
*/
    fun setArrayMainParam(listArrayOfMainParam:List<String>){
        _arrFavoriteMainParam.value = arrayListOf()
        for((c, a) in listArrayOfMainParam.withIndex()){
            _arrFavoriteMainParam.value!!.add(MainParam(a, false, c))
        }
        _arrFavoriteMainParam.value!![0].visible = true
    }
    /**
        Установка состояние загрузки
    */
    fun setConditionLoading(condition: Boolean){
        _loading.value = condition
    }

    fun getDataFromStorage() {
        _mainParam.value = getMainParamFromStorage.execute()
        _arrFavoriteMainParam.value = getFavoriteMainParamsFromStorage.execute()
        _weekTimeTable.value = getLastWeekTimeTableFromStorage.execute()
    }

    fun setDataInStorage(){
        setDataInStorage.execute(
            _mainParam.value,
            _arrFavoriteMainParam.value,
            _weekTimeTable.value
        )
    }
}