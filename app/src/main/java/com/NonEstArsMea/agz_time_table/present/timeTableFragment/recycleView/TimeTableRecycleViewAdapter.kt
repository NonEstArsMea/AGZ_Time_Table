package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass

class TimeTableRecycleViewAdapter(val withDate: Boolean = false) :
    ListAdapter<CellClass, RecyclerView.ViewHolder>(
        TimeTableItemDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CellClass.LESSON_CELL_TYPE -> {
                if (withDate) {
                    TimeTableDateLessonViewHolder(
                        inflater.inflate(
                            R.layout.date_one_lesson_card, parent, false
                        )
                    )
                } else {
                    TimeTableLessonViewHolder(
                        inflater.inflate(
                            R.layout.one_lesson_card, parent, false
                        )
                    )
                }
            }

            else -> BreakCellViewHolder(inflater.inflate(R.layout.break_cell_layout, parent, false))
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TimeTableLessonViewHolder -> {
                holder.bind(getItem(position), holder.view.context)
                holder.view.setOnClickListener {
                    setAnimation(holder.add_info)
                }
            }
            is TimeTableDateLessonViewHolder -> {
                holder.bind(getItem(position), holder.view.context)
                holder.view.setOnClickListener {
                    setAnimation(holder.add_info)
                }
            }
            is BreakCellViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<CellClass>,
        currentList: MutableList<CellClass>
    ) {
        super.onCurrentListChanged(previousList, currentList)

        Log.e("new2", currentList.toString())
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).cellType
    }

    private fun setAnimation(view: LinearLayout) {

        if (view.visibility == View.GONE ) {
            // Измеряем высоту контента
            view.visibility = View.VISIBLE
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val targetHeight = view.measuredHeight

            // Начальная высота = 0
            view.layoutParams.height = 0

            // Анимация расширения
            val expandAnimator = ValueAnimator.ofInt(0, targetHeight).apply {
                duration = 400
                addUpdateListener { animation ->
                    view.layoutParams.height = animation.animatedValue as Int
                    view.requestLayout()
                }
            }
            expandAnimator.start()

        } else {
            // Анимация схлопывания
            val initialHeight = view.height

            val collapseAnimator = ValueAnimator.ofInt(initialHeight, 0).apply {
                duration = 300
                addUpdateListener { animation ->
                    view.layoutParams.height = animation.animatedValue as Int
                    view.requestLayout()
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.GONE
                    }
                })
            }
            collapseAnimator.start()
        }
    }


}
