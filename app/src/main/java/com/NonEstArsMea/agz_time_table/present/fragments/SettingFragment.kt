package com.NonEstArsMea.agz_time_table.present.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.SettingLayoutBinding
import com.NonEstArsMea.agz_time_table.present.MainViewModel
import com.NonEstArsMea.agz_time_table.present.adapters.RecycleViewOnSearchFragmentAdapter
import com.NonEstArsMea.agz_time_table.present.adapters.SettingViewAdapter
import com.google.android.material.search.SearchView

class SettingFragment: Fragment() {
    private var _binding: SettingLayoutBinding? = null
    private val binding get() = _binding!!

    private val vm: MainViewModel by activityViewModels()

    private val rvSettingViewAdapter = SettingViewAdapter()




    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.searchBar.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.myTelegramButton.setOnClickListener {
            actionViewStart("https://t.me/delonevogne")
        }

        binding.telegramIcon.setOnClickListener {
            actionViewStart("https://t.me/delonevogne")
        }

        binding.agzButton.setOnClickListener {
            actionViewStart("https://amchs.ru/")
        }

        binding.agzIcon.setOnClickListener {
            actionViewStart("https://amchs.ru/")
        }

        binding.botButton.setOnClickListener {
            actionViewStart("https://t.me/timeagzbot")
        }

        binding.myTgIcon.setOnClickListener {
            actionViewStart("https://t.me/timeagzbot")
        }

        // recycleView
        val rvSettingView = binding.rvSettingView
        rvSettingView.adapter = rvSettingViewAdapter


        val layoutManager = LinearLayoutManager(context)
        rvSettingView.layoutManager = layoutManager
        // Проверка на null
        if(vm.arrFavoriteMainParam.value != null) {
            rvSettingViewAdapter.submitList(vm.arrFavoriteMainParam.value!!.toList())
        }
        // Обзервер
        vm.arrFavoriteMainParam.observe(viewLifecycleOwner){
            Log.e("observe", true.toString())
            rvSettingViewAdapter.submitList(it.toList())

        }

        rvSettingViewAdapter.onClickListener = { name ->
            vm.setMainParam(name)
            vm.moveItemInFavoriteMainParam(name)
        }
        rvSettingViewAdapter.onDelClickListener = {
            vm.delParamFromFavoriteMainParam(it)
        }
        rvSettingView.adapter = rvSettingViewAdapter

        return view
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //Log.e("my_tag","Search destroy")
    }

    private fun actionViewStart(uri : String){
        val link = Intent (ACTION_VIEW, Uri.parse(uri))
        startActivity(link)
    }
}