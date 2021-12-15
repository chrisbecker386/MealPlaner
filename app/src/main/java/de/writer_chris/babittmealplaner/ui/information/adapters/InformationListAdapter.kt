package de.writer_chris.babittmealplaner.ui.information.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import de.writer_chris.babittmealplaner.data.utility.Information
import de.writer_chris.babittmealplaner.databinding.ItemInformationBinding


class InformationListAdapter(private val data: List<Information>) : BaseAdapter() {
    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Information? = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding =
            ItemInformationBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        val item = getItem(position) as Information
        binding.apply {
            txtViewFirstElement.text = item.info
            txtViewSecondElement.text = item.value
        }
        return binding.root
    }
}