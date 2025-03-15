package com.NonEstArsMea.agz_time_table.present.examsFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.databinding.ExamsLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.TimeTableRecycleViewAdapter
import javax.inject.Inject

class ExamsFragment : Fragment() {

    private lateinit var onStartAndFinishListener: OnStartAndFinishListener

    private val adapter = TimeTableRecycleViewAdapter()
    private var _binding: ExamsLayoutBinding? = null


    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    lateinit var vm: ExamsFragmentViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val binding get() = _binding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(this, viewModelFactory)[ExamsFragmentViewModel::class.java]
        if (context is ExamsFragment.OnStartAndFinishListener) {
            onStartAndFinishListener = context
        } else throw RuntimeException("$context is empty")


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                onStartAndFinishListener.closeFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getTimeTable()
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

        vm.timeTableChanged.observe(viewLifecycleOwner) {
            Log.e("adapter", it.toString())
            adapter.submitList(it)
        }

        binding.exitButton.setOnClickListener {
            onStartAndFinishListener.closeFragment()
            findNavController().popBackStack()
        }

    }


    interface OnStartAndFinishListener {
        fun startFragment()
        fun closeFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}