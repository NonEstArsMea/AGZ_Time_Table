package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.viewPager.ViewPagerAdapter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ViewPagerFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewPager: ViewPager2 = requireActivity().findViewById(R.id.view_pager_time_table_fragment)
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                context?.let { context ->
                    days.toList().forEach {
                        it.setTextAppearance(R.style.MainTextViewStyle_WeekNumber)
                        it.background =
                            resources.getDrawable(R.drawable.main_surface, context.theme)
                    }
                    with(days[position]) {
                        this.setTextAppearance(com.NonEstArsMea.agz_time_table.R.style.MainTextViewStyle_DayNowWeekNumber)
                        this.background =
                            resources.getDrawable(com.NonEstArsMea.agz_time_table.R.drawable.break_cell_background, context.theme)
                    }
                }

            }
        })

        return inflater.inflate(R.layout.view_pager, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}