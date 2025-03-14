package com.NonEstArsMea.agz_time_table.present.workloadLayout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.WorkloadLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.CafTimeTableViewModel
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView.WorkloadRWAdapter
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

class WorkloadFragment : Fragment() {

    private var _binding: WorkloadLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: WorkloadViewModel

    @Inject
    lateinit var timeTableViewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    private val rwAdapter = WorkloadRWAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(
            this,
            timeTableViewModelFactory
        )[WorkloadViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WorkloadLayoutBinding.inflate(inflater, container, false)

        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, result ->
            val name = result.getString(SELECTED_ITEM) ?: ERROR_ITEM
            vm.getData(name)
            binding.workloadButtonText.text = name
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView
            duration = 1500
            scrimColor = android.graphics.Color.TRANSPARENT
        }

    }

    override fun onStart() {
        super.onStart()

        binding.workloadLayoutExitButton.setOnClickListener {
            exitTransition = MaterialContainerTransform().apply {
                duration = 300
            }

            requireActivity().supportFragmentManager.executePendingTransactions()
            startPostponedEnterTransition()
            findNavController().popBackStack()
        }


        val workloadRW = binding.recyclerViewWorkloadLayout
        workloadRW.adapter = rwAdapter

        binding.workloadChangeMainParamButton.setOnClickListener {
            val extras = FragmentNavigator.Extras.Builder()
                .addSharedElement(it, WORKLOAD_MORPH_KEY)
                .build()

            val bundle = Bundle().apply {
                putInt(
                    CafTimeTableViewModel.BUNDLE_KEY,
                    CafTimeTableViewModel.TEACHER_LIST_KEY_FOR_WORKLOAD
                )
            }
            findNavController().navigate(R.id.searchFragment, bundle, null, extras)
        }


    }

    companion object {
        private const val WORKLOAD_MORPH_KEY = "morph_shared"
        const val REQUEST_KEY = "RK"
        const val SELECTED_ITEM = "SI"
        const val ERROR_ITEM = "Не выбрано"
    }

}