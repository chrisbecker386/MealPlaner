package de.writer_chris.babittmealplaner.ui.mealplaner.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.ItemMealDayPdfBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.models.DayMealsAndDish


class PdfListAdapter(
    private val dataSource: List<DayMealsAndDish?>
) : BaseAdapter() {

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): DayMealsAndDish? = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding =
            ItemMealDayPdfBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        val item = getItem(position) as DayMealsAndDish
        binding.apply {

            date.text = CalendarUtil.longToGermanDate(item.date)
            breakfast.text = item.breakfast.dish?.dishName ?: "-"
            lunch.text = item.lunch.dish?.dishName ?: "-"
            dinner.text = item.dinner.dish?.dishName ?: "-"
        }
        return binding.root
    }
}