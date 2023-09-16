package com.NonEstArsMea.agz_time_table.present

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.CheckNetConnection
import com.NonEstArsMea.agz_time_table.databinding.MainLayoutBinding
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.isInternetConnected
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    var calendar: Calendar = Calendar.getInstance()

    private var _binding: MainLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var checkNetConnection: CheckNetConnection
    private var data: String? = null

    private lateinit var analytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        analytics = Firebase.analytics

        val day_n = calendar.get(Calendar.DAY_OF_MONTH)
        val month_n = calendar.get(Calendar.MONTH)
        val year_n = calendar.get(Calendar.YEAR)

        val vm = ViewModelProvider(this)[MainViewModel::class.java]
        vm.setCalendar(calendar = calendar)

        val pref = getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        if (pref != null) {
            vm.setMainParam(pref.getString("mainParam", "213").toString())
            val jsonString = pref.getString("listOfMainParam", null)
            if(jsonString != null){
                vm.setArrayMainParam(gson.fromJson(jsonString, Array<MainParam>::class.java).toList())
            }
        }


        binding.bottomInfo.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.menu_tt -> {
                    vm.setNewCalendar(year_n, month_n, day_n)
                    popBackStack()
                    findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.timeTableFragment)
                }

                R.id.menu_favorite -> {
                    findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.favoriteParamFragment)
                }


                R.id.menu_set_date -> {
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val month = calendar.get(Calendar.MONTH)
                    val year = calendar.get(Calendar.YEAR)
                    val dialog = DatePickerDialog(this,
                        {
                                _, year, month, dayOfMonth ->
                            vm.setNewCalendar(year, month, dayOfMonth)
                            findNavController(R.id.fragmentContainerView)
                                .navigate(R.id.timeTableFragment)
                        }, year, month, day)
                    dialog.show()
                }

                R.id.menu_setting ->{
                    popBackStack()
                    findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.settingFragment)

                }

            }
            return@setOnItemSelectedListener true
        }


        Log.e("my-tag", "connecting - " + isInternetConnected(this).toString())
        if (isInternetConnected(this)) {
            vm.loadDataFromURL()
        }

        vm.mainParam.observe(this){
            findNavController(R.id.fragmentContainerView)
                .navigate(R.id.timeTableFragment)
        }


    }

    private fun callNetConnection() {

        checkNetConnection = CheckNetConnection(application)
        checkNetConnection.observe(this) { isConnected ->
            if (isConnected and (data == null)) {

            } else {
                Toast.makeText(
                    this,
                    "Не удалось проверить обновления",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }


    private fun popBackStack() {
        findNavController(R.id.fragmentContainerView).popBackStack()
    }

    override fun onPause() {
        super.onPause()

        val vm = ViewModelProvider(this).get(MainViewModel::class.java)
        Log.e("AAA",vm.getMainParam())
        val SP = getPreferences(Context.MODE_PRIVATE)
        val editor = SP.edit()

        editor.putString("mainParam", vm.getMainParam() )
        val gson = Gson()
        val array = gson.toJson(vm.getListOfFavoriteMainParam())
        editor.putString("listOfMainParam", array )
        editor.apply()
    }
    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}

