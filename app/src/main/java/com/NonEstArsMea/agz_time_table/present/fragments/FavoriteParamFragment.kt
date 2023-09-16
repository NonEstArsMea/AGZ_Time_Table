package com.NonEstArsMea.agz_time_table.present.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.databinding.FavoriteParamLayoutBinding
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.present.MainViewModel
import com.NonEstArsMea.agz_time_table.present.adapters.RecycleViewOnSearchFragmentAdapter

class FavoriteParamFragment: Fragment() {

    private var _binding: FavoriteParamLayoutBinding? = null
    private val binding get() = _binding!!

    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteParamLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvSearchView = binding.recycleViewOnSearchFragment
        var mainParamAdapter = RecycleViewOnSearchFragmentAdapter()

        val layoutManager = LinearLayoutManager(context)
        rvSearchView.layoutManager = layoutManager



        vm.arrFavoriteMainParam.observe(viewLifecycleOwner) {
            val arr = arrayListOf<MainParam>()
            it.forEach { arr.add(it) }
            mainParamAdapter.updateData(arr)
            mainParamAdapter.onMainParamClickListener = { mainParam ->
                vm.setMainParam(mainParam)
            }
            mainParamAdapter.onMainParamLongClickListener = {position ->
                vm.updateListOfMainParam(position)
                arr.clear()
                arr.addAll(vm.getListOfFavoriteMainParam())
                mainParamAdapter.updateData(arr)
            }
            rvSearchView.adapter = mainParamAdapter


        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //Log.e("my_tag","Search destroy")
    }
}