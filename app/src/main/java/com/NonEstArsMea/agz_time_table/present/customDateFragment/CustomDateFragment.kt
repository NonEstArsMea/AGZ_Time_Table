package com.NonEstArsMea.agz_time_table.present.customDateFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.CastomDateFragmentBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.TimeTableRecycleViewAdapter
import javax.inject.Inject

class CustomDateFragment : Fragment() {
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var mainParam: String = ""

    private val args by navArgs<CustomDateFragmentArgs>()

    private lateinit var onStartAndFinishListener: OnStartAndFinishListener

    private val adapter = TimeTableRecycleViewAdapter()
    private var _binding: CastomDateFragmentBinding? = null

    private lateinit var vm: CustomDateFragmentViewModel
    private val binding get() = _binding!!

    @Inject
    lateinit var customDateFragmentViewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
        if (context is OnStartAndFinishListener) {
            onStartAndFinishListener = context
        } else throw RuntimeException("$context is empty")

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                onStartAndFinishListener.closeFragment()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        vm = ViewModelProvider(
            this,
            customDateFragmentViewModelFactory
        )[CustomDateFragmentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.let {
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
        _binding = CastomDateFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onStartAndFinishListener.startFragment()
        val recycleView = binding.recyclerViewOnCastomDateFragment
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(context)

        vm.timeTableChanged.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.exitButtom.setOnClickListener {
            onStartAndFinishListener.closeFragment()
            findNavController().popBackStack()
        }

        binding.dateText.text = vm.getDate(day, month, year)

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