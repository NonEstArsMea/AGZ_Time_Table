package com.NonEstArsMea.agz_time_table.present.departmentalTimetableFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.databinding.DepartmentalTimeTableLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.ConnectionError
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.LoadData
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.TimeTableIsLoad
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.TimeTableViewModel
import javax.inject.Inject

class DepartmentalTimeTableFragment : Fragment() {

    private var _binding: DepartmentalTimeTableLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: DepartmentalTimeTableViewModel

    @Inject
    lateinit var timeTableViewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(
            requireParentFragment(),
            timeTableViewModelFactory
        )[DepartmentalTimeTableViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DepartmentalTimeTableLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        vm.getTimeTable("71", "14-08-2024")
        observeViewModel()

        binding.departmentalTimetableTopText.setOnClickListener {
            showListDialog()
        }
        binding.exitButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun observeViewModel() {
        vm.state.observe(viewLifecycleOwner) {
            when (it) {
                is LoadData -> {}

                is ConnectionError -> {}

                is TimeTableIsLoad -> {
                }
            }
        }
    }

    private fun showListDialog() {
        val items = vm.getDepartment()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите кафедру")
            .setItems(items.toTypedArray()) { _, which ->
                // Устанавливаем выбранный элемент в ViewModel
                vm.setText(items[which])
            }
            .show()
    }
}