package com.NonEstArsMea.agz_time_table.present.customDateView

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.FragmentCastomDateBinding
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.TimeTableRecycleViewAdapter
import java.util.Calendar

class CastomDateFragment : Fragment() {
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var mainParam: String = ""

    private val args by navArgs<CastomDateFragmentArgs>()

    private lateinit var onStartAndFinishListener: OnStartAndFinishListener

    private val adapter = TimeTableRecycleViewAdapter()
    private var _binding: FragmentCastomDateBinding? = null

    private val vm: CastomDateFragmentViewModel by lazy {
        ViewModelProvider(this)[CastomDateFragmentViewModel::class.java]
    }
    private val binding get() = _binding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnStartAndFinishListener){
            onStartAndFinishListener = context
        }else throw RuntimeException( "$context is empty")

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args?.let {
            day = it.day
            month = it.month
            year = it.year
            mainParam = it.mainParam
        }
        vm.getTimeTable(day, month, year, mainParam)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCastomDateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onStartAndFinishListener.startFragment()
        val recycleView = binding.recyclerViewOnCastomDateFragment
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(context)

        vm.timeTableChanged.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.exitButtom.setOnClickListener {
            onStartAndFinishListener.closeFragment()
            findNavController().popBackStack()
        }

    }


    interface OnStartAndFinishListener{
        fun startFragment()
        fun closeFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}