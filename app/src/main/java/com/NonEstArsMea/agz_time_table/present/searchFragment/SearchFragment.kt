package com.NonEstArsMea.agz_time_table.present.searchFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.net.retrofit.Common
import com.NonEstArsMea.agz_time_table.databinding.SearchLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.searchFragment.recycleView.RecycleViewOnSearchFragmentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchFragment : Fragment() {
    private var _binding: SearchLayoutBinding? = null
    private val binding get() = _binding!!

    lateinit var vm: SearchViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private val mainParamAdapter = RecycleViewOnSearchFragmentAdapter()


    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
        vm = ViewModelProvider(this,viewModelFactory)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSearchView = binding.recycleViewOnSearchFragment
        rvSearchView.adapter = mainParamAdapter
        rvSearchView.layoutManager = LinearLayoutManager(context)

        val searchView = binding.searchView
        searchView.isIconifiedByDefault = false

        vm.listOfMainParam.observe(viewLifecycleOwner) {
            Common.retrofitService.getMainParamsList().enqueue(object : Callback<MutableList<String>>{
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    Log.e("retro", (response.body()).toString())
                }

                override fun onFailure(p0: Call<MutableList<String>>, p1: Throwable) {

                }
            })
            mainParamAdapter.submitList(it)
            searchView.setOnQueryTextListener(object :
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?) = false

                override fun onQueryTextChange(text: String): Boolean {
                    val aCopy = it.toMutableList()
                    aCopy.retainAll { newText ->
                        text in newText.name.lowercase()
                    }
                    mainParamAdapter.submitList(aCopy)
                    return false
                }
            })
        }

        // Нажатие на объекты
        mainParamAdapter.onMainParamClickListener = { mainParam ->
            vm.setNewMainParam(mainParam)
            findNavController().popBackStack(R.id.timeTableFragment, false)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}