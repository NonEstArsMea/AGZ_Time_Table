package com.NonEstArsMea.agz_time_table.present.examsFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.databinding.ExamsLayoutBinding
import com.NonEstArsMea.agz_time_table.present.customDateFragment.CastomDateFragment
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.TimeTableRecycleViewAdapter

class ExamsFragment: Fragment() {

    private var mainParam: String = ""

    private val args by navArgs<ExamsFragmentArgs>()

    private lateinit var onStartAndFinishListener: CastomDateFragment.OnStartAndFinishListener

    private val adapter = TimeTableRecycleViewAdapter()
    private var _binding: ExamsLayoutBinding? = null

    private val vm: ExamsFragmentViewModel by lazy {
        ViewModelProvider(this)[ExamsFragmentViewModel::class.java]
    }
    private val binding get() = _binding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CastomDateFragment.OnStartAndFinishListener){
            onStartAndFinishListener = context
        }else throw RuntimeException( "$context is empty")

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args?.let {
            mainParam = it.nameParam
        }
        vm.getTimeTable(mainParam)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ExamsLayoutBinding.inflate(layoutInflater)
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