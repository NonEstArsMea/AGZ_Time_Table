package com.NonEstArsMea.agz_time_table.present.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toLowerCase
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.SearchLayoutBinding
import com.NonEstArsMea.agz_time_table.databinding.SettingLayoutBinding
import com.NonEstArsMea.agz_time_table.present.MainViewModel
import com.NonEstArsMea.agz_time_table.present.adapters.RecycleViewOnSearchFragmentAdapter
import com.google.android.material.search.SearchView

class SearchFragment: Fragment() {
    private var _binding: SearchLayoutBinding? = null
    private val binding get() = _binding!!
    private val vm: MainViewModel by activityViewModels()
    private val mainParamAdapter = RecycleViewOnSearchFragmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        val rvSearchView = binding.recycleViewOnSearchFragment

        val searchView = binding.searchView
        searchView.requestFocus()
        searchView.isIconified = false
        rvSearchView.adapter = mainParamAdapter

        val listOfMainParam = vm.listOfMainParam.value!!.toMutableList()

        mainParamAdapter.updateData(listOfMainParam)
        // Нажатие на объекты
        mainParamAdapter.onMainParamClickListener = { mainParam ->
            Log.e("main", mainParamAdapter.onMainParamClickListener.toString())
            vm.updateListOfFavoriteMainParam(mainParam)
            vm.setMainParam(mainParam)
            findNavController().navigate(R.id.settingFragment)
        }


        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(text: String): Boolean {
                val aCopy = listOfMainParam.toMutableList()
                aCopy.retainAll {
                    text.toString() in it.name.lowercase()
                }
                mainParamAdapter.updateData(aCopy)
                mainParamAdapter.onMainParamClickListener = { mainParam ->
                    vm.updateListOfFavoriteMainParam(mainParam)
                    vm.setMainParam(mainParam)
                    findNavController().navigate(R.id.settingFragment)
                }
                return false
            }
        })


        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //Log.e("my_tag","Search destroy")
    }
}