    package com.NonEstArsMea.agz_time_table.present.timeTableFragment

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
    import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
    import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
    import com.NonEstArsMea.agz_time_table.data.StorageRepositoryImpl
    import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
    import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl.getListOfMainParam
    import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetLastWeekFromeStorageUseCase
    import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetMainParamFromStorageUseCase
    import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetWeekTimeTableListUseCase
    import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
    import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.Job
    import kotlinx.coroutines.SupervisorJob
    import kotlinx.coroutines.launch
    import java.util.Calendar

    class TimeTableViewModel(
        private val getWeekTimeTableUseCase: GetWeekTimeTableListUseCase,
    ): ViewModel() {


        private var job: Job = viewModelScope.launch {  }


        private var _timeTableChanged = TimeTableRepositoryImpl.getArrayOfWeekTimeTable()
        val timeTableChanged: LiveData<List<List<CellApi>>>
            get() = _timeTableChanged

        // хранит список с расписанием
        val dataLiveData = DataRepositoryImpl.getData()

        // хранит состояние загрузки
        private val _loading = MutableLiveData<Boolean>()
        val loading: LiveData<Boolean>
            get() = _loading


        private var dataIsLoad: Boolean = false

        private var dataWasChanged: Boolean = true

        private var lastMainParam: String = ""


        // Хранит параметр поиска
        private val _mainParam = TimeTableRepositoryImpl.getMainParam()
        val mainParam: LiveData<MainParam>
            get() = _mainParam


        fun getMainParam():String{
            return TimeTableRepositoryImpl.getMainParam().value!!.name
        }
        // создание массивов с расписанием
        fun getTimeTable(){
                if (!dataIsLoad) {
                    getNewTimeTable()
                    viewModelScope.launch {
                        getListOfMainParam(dataLiveData.value!!)
                    }
                    dataIsLoad = true
                }
        }

        fun getNewTimeTable(newTime: Int? = null){
            if(newTime != null){
                DateRepositoryImpl.setNewCalendar(newTime)
            }
            if(dataLiveData.value != null) {
                // для прерывания предыдущих корутин
                if (job.isActive) {
                    job.cancel()
                }
                dataWasChanged = true
                job = viewModelScope.launch {
                    setConditionLoading(true)
                    _timeTableChanged.value = getWeekTimeTableUseCase.execute(dataLiveData.value!!)
                    setConditionLoading(false)
                }
            }
        }

        fun checkMainParam(){
            if(lastMainParam != _mainParam.value?.name){
                lastMainParam = _mainParam.value?.name!!
                getNewTimeTable()
            }
        }


        /**
         * Остановка корутины после завершения работы
         */
        override fun onCleared() {
            super.onCleared()
            job.cancel()
        }

        /**
        Установка состояние загрузки
         */
        fun setConditionLoading(condition: Boolean){
            _loading.value = condition
        }


        fun startFragment(){
            StateRepositoryImpl.setNewMenuItem(StateRepositoryImpl.TIME_TABLE_ITEM)
        }



    }