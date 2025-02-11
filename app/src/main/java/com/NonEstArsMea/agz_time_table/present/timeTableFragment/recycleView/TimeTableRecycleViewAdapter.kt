package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass

class TimeTableRecycleViewAdapter : ListAdapter<CellClass, RecyclerView.ViewHolder>(
    TimeTableItemDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CellClass.LESSON_CELL_TYPE -> TimeTableLessonViewHolder(
                inflater.inflate(
                    R.layout.one_lesson_card, parent, false
                )
            )

            else -> BreakCellViewHolder(inflater.inflate(R.layout.break_cell_layout, parent, false))
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TimeTableLessonViewHolder -> {
                holder.bind(getItem(position), holder.view.context)
                holder.view.setOnClickListener {
                    setAnimation(holder)
                }
            }

            is BreakCellViewHolder -> holder.bind(getItem(position))
        }


    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).cellType
    }

    private fun setAnimation(holder: TimeTableLessonViewHolder) {
        val targetView = holder.add_info

        if (targetView.visibility == View.GONE) {
            // Измеряем высоту контента
            targetView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val targetHeight = targetView.measuredHeight

            // Начальная высота = 0
            targetView.layoutParams.height = 0
            targetView.visibility = View.VISIBLE

            // Анимация расширения
            val expandAnimator = ValueAnimator.ofInt(0, targetHeight).apply {
                duration = 400
                addUpdateListener { animation ->
                    targetView.layoutParams.height = animation.animatedValue as Int
                    targetView.requestLayout()
                }
            }
            expandAnimator.start()
        } else {
            // Анимация схлопывания
            val initialHeight = targetView.height

            val collapseAnimator = ValueAnimator.ofInt(initialHeight, 0).apply {
                duration = 300
                addUpdateListener { animation ->
                    targetView.layoutParams.height = animation.animatedValue as Int
                    targetView.requestLayout()
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        targetView.visibility = View.GONE
                    }
                })
            }
            collapseAnimator.start()
        }
    }

    companion object {
        const val LESSON_TIME_TABLE_TYPE = 0
        const val BREAK_CELL_TYPE = 1
    }

}
