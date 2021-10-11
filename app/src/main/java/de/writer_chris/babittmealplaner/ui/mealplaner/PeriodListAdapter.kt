package de.writer_chris.babittmealplaner.ui.mealplaner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.ItemPeriodBinding

class PeriodListAdapter(private val onItemClick: (Period) -> Unit) :
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
                return oldItem.startDate == newItem.startDate &&
                        oldItem.endDate == newItem.endDate
            }
        }
    }

    class PeriodViewHolder(private var binding: ItemPeriodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(period: Period) {
            binding.apply {
                txtStartWeekday.setText(CalendarUtil.longToWeekdayResId(period.startDate))
                txtStartDate.text = CalendarUtil.longToGermanDate(period.startDate)
                txtEndWeekday.setText(CalendarUtil.longToWeekdayResId(period.endDate))
                txtEndDate.text = CalendarUtil.longToGermanDate(period.endDate)
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
        val toast = Toast.makeText(holder.itemView.context, "dada", LENGTH_SHORT)
        holder.itemView.setOnClickListener { toast.show() }
        holder.bind(current)
    }


}