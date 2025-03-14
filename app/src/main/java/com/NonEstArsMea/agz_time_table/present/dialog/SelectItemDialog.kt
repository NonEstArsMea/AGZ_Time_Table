package com.NonEstArsMea.agz_time_table.present.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.databinding.DialogSelectItemBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.CafTimeTableFragment
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.CafTimeTableViewModel
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class SelectItemDialog : BottomSheetDialogFragment() {

    lateinit var vm: CafTimeTableViewModel

    lateinit var binding: DialogSelectItemBinding

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }


    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)


        vm = ViewModelProvider(
            requireParentFragment(),
            viewModelFactory
        )[CafTimeTableViewModel::class.java]

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSelectItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = ItemsAdapter(
            CafTimeTableFragment.names,
            CafTimeTableFragment.items
        ) { selectedItem, id ->
            vm.setNewID(id)
            vm.getData(selectedItem, id)
            dismiss()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }


}