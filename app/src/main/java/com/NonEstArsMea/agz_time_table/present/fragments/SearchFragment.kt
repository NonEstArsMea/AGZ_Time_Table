package com.NonEstArsMea.agz_time_table.present.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.SearchFragmentBinding
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.present.MainViewModel
import com.NonEstArsMea.agz_time_table.present.adapters.RecycleViewOnSearchFragmentAdapter

class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvSearchView = binding.recycleViewOnSearchFragment
        var mainParamAdapter = RecycleViewOnSearchFragmentAdapter()

        val layoutManager = LinearLayoutManager(context)
        rvSearchView.layoutManager = layoutManager



        vm.listOfMainParam.observe(viewLifecycleOwner) {
            mainParamAdapter.updateData(it.toMutableList())
            mainParamAdapter.onMainParamClickListener = { mainParam ->
                vm.setMainParam(mainParam)
                //findNavController().navigate(R.id.timeTableFragment)
            }
            mainParamAdapter.onMainParamLongClickListener = {position ->
                vm.updateListOfMainParam(position)
                mainParamAdapter.updateData(it.toMutableList())
            }
            rvSearchView.adapter = mainParamAdapter

            val searchView = binding.searchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val aCopy = it.toMutableList<MainParam>()
                    aCopy.retainAll {
                        newText.toString() in it.name.lowercase()
                    }
                    mainParamAdapter.updateData(aCopy)
                    mainParamAdapter.onMainParamClickListener = { mainParam ->
                        vm.setMainParam(mainParam)
                        findNavController().navigate(R.id.timeTableFragment)
                    }
                    rvSearchView.adapter = mainParamAdapter

                    return false
                }
            })
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //Log.e("my_tag","Search destroy")
    }

}