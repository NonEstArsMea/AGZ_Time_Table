package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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
            LESSON_TIME_TABLE_TYPE -> TimeTableLessonViewHolder(
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
        return if (getItem(position).text == "") {
            LESSON_TIME_TABLE_TYPE
        } else {
            BREAK_CELL_TYPE
        }
    }

    private fun setAnimation(holder: TimeTableLessonViewHolder) {
        if (holder.add_info.visibility == View.GONE) {
            // Раскрытие дополнительного контента с анимацией
            val expandAnimation = ObjectAnimator.ofPropertyValuesHolder(
                holder.add_info,
                PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
            )
            expandAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    holder.add_info.visibility = View.VISIBLE
                }
            })
            expandAnimation.duration = 400
            expandAnimation.start()
        } else {
            // Скрытие дополнительного контента с анимацией
            val collapseAnimation = ObjectAnimator.ofPropertyValuesHolder(
                holder.add_info,
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f)
            )
            collapseAnimation.duration = 300
            collapseAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    holder.add_info.visibility = View.GONE
                }
            })
            collapseAnimation.start()
        }
    }

    companion object {
        const val LESSON_TIME_TABLE_TYPE = 0
        const val BREAK_CELL_TYPE = 1
    }

}
