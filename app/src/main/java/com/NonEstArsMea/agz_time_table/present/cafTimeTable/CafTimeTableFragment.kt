package com.NonEstArsMea.agz_time_table.present.cafTimeTable

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.CafTimeTableLayoutBinding
import com.NonEstArsMea.agz_time_table.databinding.DialogSelectItemBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.util.animateSlideText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class CafTimeTableFragment : Fragment() {

    private var _binding: CafTimeTableLayoutBinding? = null
    private val binding get() = _binding!!

    lateinit var vm: CafTimeTableViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(this, viewModelFactory)[CafTimeTableViewModel::class.java]

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = CafTimeTableLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeVM()

    }

    override fun onResume() {
        super.onResume()


        binding.cafTimeTableButton.setOnClickListener {
            SelectItemDialog().show(childFragmentManager, "tag")
        }

        binding.btnLeft.setOnClickListener {
            vm.setNewDate(-1)
        }

        binding.btnRight.setOnClickListener {
            vm.setNewDate(1)
        }

    }

    private fun observeVM() {
        vm.state.observe(viewLifecycleOwner) {
            when (it) {
                ConnectionError -> TODO()
                is SetDate -> {
                    binding.dateText.text = DateManager.getDateString(requireContext(), it.date)
                    if (it.id.isNotEmpty()) binding.cafTimeTableButtonText.animateSlideText(it.id)
                    binding.cafTimeTableView.clearView()
                }

                is DataIsLoad -> {
                    binding.dateText.text = DateManager.getDateString(requireContext(), it.date)
                    binding.cafTimeTableView.setCafTimeTable(it.rep, it.unicList)
                    binding.cafTimeTableButtonText.animateSlideText(it.cafName)
                }

                else -> {}
            }
        }
    }


}

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


        val adapter = ItemsAdapter() { selectedItem, id ->
            vm.setNewID(id)
            vm.getData(selectedItem, id)
            dismiss()  // Закрываем диалог
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }
}

class ItemsAdapter(
    private val onItemClick: (String, String) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.rv_search_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_on_search_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = names[position]
        holder.textView.text = item
        holder.itemView.setOnClickListener { onItemClick(item, items[position]) }
    }

    override fun getItemCount() = items.size

    companion object {
        val names =
            listOf(
                "(№11) Кафедра оперативного управления мероприятиями РСЧС и ГО",
                "(№12) Кафедра медико-биологической и экологической защиты",
                "(№13) Кафедра инженерной защиты населения и территорий",
                "(№14) Кафедра организации управления повседневной деятельности МЧС России",
                "(№21) Кафедра тактики и общевоенных дисциплин",
                "(№22) Кафедра аварийно-спасательных работ",
                "(№23) Кафедра радиационной и химической защиты",
                "(№25) Кафедра спасательных робототехнических средств",
                "(№31) Кафедра информационных систем и технологий",
                "(№32) Кафедра аэронавигации и беспилотных авиационных систем",
                "(№34) Кафедра эксплуатации транспортно-технологических машин и комплексов",
                "(№35) Кафедра инфокоммуникационных технологий и систем связи",
                "(№36) Кафедра информатики и вычислительной техники",
                "(№41) Кафедра экономики, менеджмента и организации государственных закупок",
                "(№42) Кафедра педагогики и психологии",
                "(№43) Кафедра юридических дисциплин",
                "(№44) Кафедра государственного и муниципального управления",
                "(№45) Кафедра рекламы и связей с общественностью",
                "(№46) Кафедра философии, истории и культурологии",
                "(№51) Кафедра пожарной безопасности",
                "(№62) Кафедра иностранных языков",
                "(№71) Кафедра устойчивости экономики и систем жизнеобеспечения",
                "(№72) Кафедра высшей математики",
                "(№74) Кафедра механики и инженерной графики",
                "(№75) Кафедра физической подготовки и спорта",
                "(№76) Кафедра физики",
                "(№77) Кафедра химии и материаловедения",
                "(№81) Кафедра мобилизационной подготовки",
                )

        val items =
            listOf(
                "11",
                "12",
                "13",
                "14",
                "21",
                "22",
                "23",
                "25",
                "31",
                "32",
                "34",
                "35",
                "36",
                "41",
                "42",
                "43",
                "44",
                "45",
                "46",
                "24",
                "73",
                "71",
                "72",
                "74",
                "75",
                "76",
                "77",
                "79",
            )
    }
}
