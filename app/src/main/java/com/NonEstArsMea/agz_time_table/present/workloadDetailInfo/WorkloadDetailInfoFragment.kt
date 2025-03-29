package com.NonEstArsMea.agz_time_table.present.workloadDetailInfo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Transition.TransitionListener
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.ExamsLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.TimeTableRecycleViewAdapter
import com.NonEstArsMea.agz_time_table.present.workloadLayout.WorkloadFragment
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.util.Methods
import com.NonEstArsMea.agz_time_table.util.getFullName
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

class WorkloadDetailInfoFragment : Fragment() {

    private val adapter = TimeTableRecycleViewAdapter(true)
    private var _binding: ExamsLayoutBinding? = null
    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    lateinit var vm: WorkloadDetailInfoViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(this, viewModelFactory)[WorkloadDetailInfoViewModel::class.java]

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView
            duration = 1000
            scrimColor = android.graphics.Color.TRANSPARENT // Убирает затемнение фона
        }
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView
            duration = 1500
            scrimColor = android.graphics.Color.TRANSPARENT
        }

        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ExamsLayoutBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        val morphName = bundle?.getString(WorkloadFragment.MORPH_KEY) ?: "default_morph"
        val department = bundle?.getString(WorkloadFragment.DEPARTMENT_KEY) ?: WorkloadFragment.ERROR_ITEM
        val month = bundle?.getString(WorkloadFragment.MONTH_KEY) ?: ""
        val mainParam = bundle?.getString(WorkloadFragment.MAIN_PARAM_KEY) ?: ""
        vm.getData(month, department, mainParam)
        binding.viewName.text = department.getFullName() + " - " + DateManager.getMonthNominativeСaseByString(month)
        binding.cardView.transitionName = morphName

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerViewOnCastomDateFragment.adapter = adapter
        binding.recyclerViewOnCastomDateFragment.layoutManager = LinearLayoutManager(context)

        binding.exitButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    @SuppressLint("SetTextI18n")
    fun observeVM() {
        vm.state.observe(viewLifecycleOwner) {
            when (it) {
                is DataIsLoad -> {
                    adapter.submitList(it.list)
                    binding.viewName.text =
                        it.name.getFullName() + " " + DateManager.getMonthNominativeСaseByString(it.month)
                }

                is LoadData -> {
                    binding.viewName.text =
                        it.name.getFullName()
                }
            }
        }
    }
}