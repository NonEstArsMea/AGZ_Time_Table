package com.NonEstArsMea.agz_time_table.present.settingFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.AuthRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.SettingLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView.SettingRecycleViewAdapter
import javax.inject.Inject

class SettingFragment : Fragment() {
    private var _binding: SettingLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var vm: SettingViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private lateinit var setSystemTheme: setThemeInterface

    private val rvSettingViewAdapter = SettingRecycleViewAdapter()

    @Inject
    lateinit var themeChecker: ThemeController


    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
        vm = ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]
        if (context is setThemeInterface) {
            setSystemTheme = context
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
            findNavController().navigate(R.id.searchFragment)
        }

        binding.audWorkloadButton.setOnClickListener {
            findNavController().navigate(R.id.audWorkloadFragment)
        }

        binding.cafTimeTableButton.setOnClickListener {
            findNavController().navigate(R.id.cafTimeTableFragment)
        }

        binding.botButton.setOnClickListener {
            actionViewStart("https://t.me/timeagzbot")
        }

        binding.telegramIcon.setOnClickListener {
            actionViewStart("https://t.me/timeagzbot")
        }

        binding.agzButton.setOnClickListener {
            actionViewStart("https://amchs.ru/")
        }

        binding.agzIcon.setOnClickListener {
            actionViewStart("https://amchs.ru/")
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

        vm.authResult.observe(viewLifecycleOwner) {
            Log.e("log", it.toString())
            when (it) {
                is AuthRepositoryImpl.UserProfile.IsLoggedIn -> setLogicIfLoggedIn(it.name)
                is AuthRepositoryImpl.UserProfile.IsLoggedOut -> setLogicIfLoggedOut()
            }
        }



        binding.toggleButton.isSingleSelection = true
        binding.toggleButton.check(themeChecker.checkTheme())
        binding.toggleButton.addOnButtonCheckedListener { toggleGroup, checkedId, isChecked ->
            themeChecker.setTheme(isChecked, checkedId)
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


    interface setThemeInterface {
        fun setLightTheme()
        fun setDarkTheme()
        fun setSystemTheme()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun actionViewStart(uri: String) {
        val link = Intent(ACTION_VIEW, Uri.parse(uri))
        startActivity(link)
    }

    private fun setLogicIfLoggedIn(name: String) {
        binding.loginCardTopText.text = name
        binding.loginCardInfoText.text = "Вы вошли в аккаунт. \nЧтобы выйти, удерживайте кнопку"
        binding.departmentalTimetable.visibility = View.VISIBLE

        binding.departmentalTimetable.setOnClickListener {
            Toast.makeText(requireActivity(), "1234", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.departmentalTimeTableFragment)
        }

        binding.loginCard.setOnLongClickListener {
            vm.logOut()
            return@setOnLongClickListener true
        }

        binding.loginCard.setOnClickListener(null)
    }

    private fun setLogicIfLoggedOut() {
        binding.loginCardTopText.text = "Вход"
        binding.loginCardInfoText.text =
            "Если у вас есть аккаунт, вы можете просматривать дополнительную информацию"
        binding.departmentalTimetable.visibility = View.GONE

        binding.loginCard.setOnLongClickListener(null)

        binding.loginCard.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }
}