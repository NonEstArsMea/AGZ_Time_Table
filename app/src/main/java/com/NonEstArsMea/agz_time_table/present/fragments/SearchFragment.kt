package com.NonEstArsMea.agz_time_table.present.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.SearchLayoutBinding
import com.NonEstArsMea.agz_time_table.present.MainViewModel
import com.NonEstArsMea.agz_time_table.present.adapters.RecycleViewOnSearchFragmentAdapter

class SearchFragment : Fragment() {
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

        val rvSearchView = binding.recycleViewOnSearchFragment
        rvSearchView.adapter = mainParamAdapter
        rvSearchView.layoutManager = LinearLayoutManager(context)

        val searchView = binding.searchView
        searchView.isIconifiedByDefault = false

        val listOfMainParam = vm.listOfMainParam.value?.toMutableList()

        mainParamAdapter.submitList(listOfMainParam)
        // Нажатие на объекты
        mainParamAdapter.onMainParamClickListener = { mainParam ->
            vm.updateListOfFavoriteMainParam(mainParam)
            vm.setMainParam(mainParam.name)
            findNavController().navigate(R.id.settingFragment)
        }


        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?) = false

            override fun onQueryTextChange(text: String): Boolean {
                val aCopy = listOfMainParam?.toMutableList()
                aCopy?.retainAll {
                    text.toString() in it.name.lowercase()
                }
                mainParamAdapter.submitList(aCopy)
                mainParamAdapter.onMainParamClickListener = { mainParam ->
                    vm.updateListOfFavoriteMainParam(mainParam)
                    vm.setMainParam(mainParam.name)
                    findNavController().navigate(R.id.settingFragment)
                }
                return false
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}