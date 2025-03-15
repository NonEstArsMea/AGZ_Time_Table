package com.NonEstArsMea.agz_time_table.present.workloadLayout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

    private val rwAdapter = WorkloadRWAdapter { month, department, view, position ->

        val extras = FragmentNavigator.Extras.Builder()
            .addSharedElement(view, WORKLOAD_DETAIL_MORPH_KEY + position.toString())
            .build()

        val bundle = Bundle().apply {
            putString(MORPH_KEY, "morph_$position")
        }
        findNavController().navigate(
            R.id.action_workloadFragment_to_workloadDetailInfoFragment,
            bundle, null, extras
        )
    }

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
            vm.sedNameInStorage(name)
            vm.getData(name)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition() // Останавливаем анимацию до полной загрузки списка

        binding.recyclerViewWorkloadLayout.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    binding.recyclerViewWorkloadLayout.viewTreeObserver.removeOnPreDrawListener(this)
                    startPostponedEnterTransition() // Запускаем анимацию, когда список готов
                    return true
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView
            duration = 1000
            scrimColor = android.graphics.Color.TRANSPARENT // Убирает затемнение фона
        }
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView
            duration = 1500
            scrimColor = android.graphics.Color.TRANSPARENT
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            duration = 300
        }

    }

    override fun onStart() {
        super.onStart()

        observeVM()

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
        workloadRW.layoutManager = LinearLayoutManager(context)

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

    private fun observeVM() {
        vm.state.observe(viewLifecycleOwner) {
            when (it) {
                is SetName -> {
                    binding.workloadButtonText.text = it.name
                }

                is DataIsLoad -> {
                    binding.workloadButtonText.text = it.name
                    rwAdapter.submitList(it.list)
                    binding.recyclerViewWorkloadLayout.adapter = rwAdapter
                }
            }
        }
    }

    companion object {
        private const val WORKLOAD_MORPH_KEY = "morph_shared"
        private const val WORKLOAD_DETAIL_MORPH_KEY = "morph_"
        const val REQUEST_KEY = "RK"
        const val MORPH_KEY = "RK"
        const val SELECTED_ITEM = "SI"
        const val ERROR_ITEM = "Не выбрано"
    }

}