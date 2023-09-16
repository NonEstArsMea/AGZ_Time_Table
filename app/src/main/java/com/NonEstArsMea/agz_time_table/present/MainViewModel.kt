package com.NonEstArsMea.agz_time_table.present

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.useCases.GetListOfMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.useCases.GetWeekTimeTableListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Calendar

class MainViewModel: ViewModel() {
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
                val data = getDataFromUrl()
                _dataLiveData.value = data
            } catch (e: Exception) {
                Log.e("my-tag", e.toString())
            }
        }
    }


    // содание соединения
    private suspend fun getDataFromUrl():  String = withContext(Dispatchers.IO) {
        val connection = URL("http://a0755299.xsph.ru/wp-content/uploads/3-1-1.txt").openConnection()
        connection.connect()
        val content = connection.getInputStream().bufferedReader().use { it.readText() }
        return@withContext content
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
                Log.e("scope", _weekTimeTable.value.toString())
                _weekTimeTable.value = getWeekTimeTableUseCase.execute(newData, dayOfWeek, mainParam)
                setConditionLoading(false)
            } catch (e: Exception) {
                Log.e("my--tag", e.toString())
            }
        }
        uiScope.launch {
            getListOfMainParam(newData)
        }
    }

    // получение списка с параметром поиска
    fun getListOfMainParam(data: String){
            val arr: ArrayList<MainParam>? = arrFavoriteMainParam.value
            Log.e("any", arr.toString())
            _listOfMainParam.value = getListOfMainParamUseCase.execute(data, arr)
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
            "213"
        }

    }

    fun setNewCalendar(year: Int, month: Int, dayOfMonth: Int){
        _calendarLiveData.value?.set(year, month, dayOfMonth)
    }

    fun getCalendar():Calendar{
        return _calendarLiveData.value!!
    }

    fun setCalendar(calendar: Calendar){
        _calendarLiveData.value = calendar
    }
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun updateListOfMainParam(position: Int) {
        Log.e("AAA",_listOfMainParam.value.toString())
        _listOfMainParam.value?.get(position)?.visible = !(_listOfMainParam.value?.get(position)?.visible)!!
        _arrFavoriteMainParam.value = getListOfFavoriteMainParam() as ArrayList<MainParam>
    }

    fun getListOfFavoriteMainParam(): List<MainParam> {

        return _listOfMainParam.value?.filter { it.visible == true } ?: arrayListOf()
    }

    fun setArrayMainParam(listArrayOfMainParam:List<MainParam>){
        _arrFavoriteMainParam.value = arrayListOf()
        _arrFavoriteMainParam.value!!.addAll(listArrayOfMainParam)
    }

    fun setConditionLoading(condition: Boolean){
        _loading.value = condition
    }
}