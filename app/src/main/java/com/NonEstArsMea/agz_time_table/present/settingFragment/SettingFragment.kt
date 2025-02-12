package com.NonEstArsMea.agz_time_table.present.settingFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.SettingLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModel
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView.SettingRecycleViewAdapter
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

class SettingFragment : Fragment() {
    private var _binding: SettingLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var vm: SettingViewModel

    @Inject
    lateinit var themeViewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val rvSettingViewAdapter = SettingRecycleViewAdapter()

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
        vm = ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]
        themeViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView
            duration = 1000
            scrimColor = android.graphics.Color.TRANSPARENT // Убирает затемнение фона
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingLayoutBinding.inflate(inflater, container, false)
        vm.startFragment()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.searchBar.setOnClickListener {
            val extras = FragmentNavigator.Extras.Builder()
                .addSharedElement(it, "morph_shared")
                .build()
            findNavController().navigate(R.id.searchFragment, null, null, extras)
        }

        binding.audWorkloadButton.setOnClickListener {
            val extras = FragmentNavigator.Extras.Builder()
                .addSharedElement(it, "morph_aud_workload")
                .build()
            findNavController().navigate(R.id.audWorkloadFragment, null, null, extras)
        }

        binding.cafTimeTableButton.setOnClickListener {
            val extras = FragmentNavigator.Extras.Builder()
                .addSharedElement(it, "morph_caf_time_table")
                .build()
            findNavController().navigate(
                R.id.action_settingFragment_to_cafTimeTableFragment,
                null,
                null,
                extras
            )
        }


        binding.botButton.setOnClickListener {
            actionViewStart("https://t.me/timeagzbot")
        }

        binding.telegramIcon.setOnClickListener {
            actionViewStart("https://t.me/timeagzbot")
        }

        binding.agzButton.setOnClickListener {
            actionViewStart("https://agzprogs.ru/")
        }

        binding.agzIcon.setOnClickListener {
            actionViewStart("https://agzprogs.ru/")
        }

        binding.myTelegramButton.setOnClickListener {
            actionViewStart("https://t.me/delonevogne")
        }

        binding.myTgIcon.setOnClickListener {
            actionViewStart("https://t.me/delonevogne")
        }

        val rvSettingView = binding.rvSettingView
        rvSettingView.adapter = rvSettingViewAdapter


        val layoutManager = LinearLayoutManager(context)
        rvSettingView.layoutManager = layoutManager

        vm.listOfFavoriteMainParam.observe(viewLifecycleOwner) {
            rvSettingViewAdapter.submitList(it.toList())
        }

        binding.toggleButton.isSingleSelection = true
        binding.toggleButton.check(themeViewModel.checkTheme())
        binding.toggleButton.addOnButtonCheckedListener { toggleGroup, checkedId, isChecked ->
            themeViewModel.setTheme(isChecked, checkedId)
        }

        rvSettingViewAdapter.onClickListener = { mainParam ->
            vm.setMainParam(mainParam)
            vm.moveItemInFavoriteMainParam(mainParam)
        }

        rvSettingViewAdapter.onDelClickListener = {
            vm.delParamFromFavoriteMainParam(it)
        }

        rvSettingView.adapter = rvSettingViewAdapter

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun actionViewStart(uri: String) {
        val link = Intent(ACTION_VIEW, Uri.parse(uri))
        startActivity(link)
    }


}