package de.writer_chris.babittmealplaner.ui.mealplaner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.ItemPeriodBinding

class PeriodListAdapter(
    private val onItemClick: (Period) -> Unit,
    private val onItemLongClick: (Period) -> Unit
) :
    ListAdapter<Period, PeriodListAdapter.PeriodViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Period>() {
            override fun areItemsTheSame(
                oldItem: Period,
                newItem: Period
            ): Boolean {
                return oldItem.periodId == newItem.periodId
            }

            override fun areContentsTheSame(
                oldItem: Period,
                newItem: Period
            ): Boolean {
                return (oldItem.startDate == newItem.startDate && oldItem.endDate == newItem.endDate)
            }
        }
    }

    class PeriodViewHolder(private var binding: ItemPeriodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(period: Period) {
            binding.apply {
                txtStartWeekday.text = CalendarUtil.longToWeekday(period.startDate)
                txtStartDate.text = CalendarUtil.longToDate(period.startDate)
                txtEndWeekday.text = CalendarUtil.longToWeekday(period.endDate)
                txtEndDate.text = CalendarUtil.longToDate(period.endDate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodViewHolder {
        return PeriodViewHolder(
            ItemPeriodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PeriodViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onItemClick(current) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(current)
            return@setOnLongClickListener true
        }
        holder.bind(current)
    }

}