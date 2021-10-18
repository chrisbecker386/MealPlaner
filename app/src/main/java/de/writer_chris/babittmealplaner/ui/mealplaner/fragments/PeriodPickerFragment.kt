package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.FragmentPeriodPickerBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.PeriodPickerViewModel
import de.writer_chris.babittmealplaner.ui.mealplaner.PeriodPickerViewModelFactory


class PeriodPickerFragment : Fragment() {

    private val viewModel: PeriodPickerViewModel by viewModels {
        PeriodPickerViewModelFactory(Repository(requireContext()), null, null)
    }

    private var _binding: FragmentPeriodPickerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeriodPickerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.startDate.observe(this.viewLifecycleOwner) {
            it.let {
                binding.txtStart.setText(CalendarUtil.calendarToWeekdayResId(it))
            }
        }
        viewModel.endDate.observe(this.viewLifecycleOwner) {
            it.let {
                binding.txtEnd.setText(CalendarUtil.calendarToWeekdayResId(it))
            }
        }
        setupPicker()

        binding.btnSaveSchedulePeriod.apply {
            setOnClickListener {
                addPeriod()
                val action =
                    PeriodPickerFragmentDirections.actionDatePickerFragmentToNavigationMeal()
                this.findNavController().navigate(action)
            }
        }
    }

    private fun setupPicker() {

        binding.apply {
            val start =
                viewModel.startDate.value ?: throw IllegalArgumentException("startDate is null")

            val end = viewModel.endDate.value ?: throw IllegalArgumentException("endDate is null")
            datePickerStart.updateDate(
                start.get(Calendar.YEAR),
                start.get(Calendar.MONTH),
                start.get(Calendar.DAY_OF_MONTH)
            )
            datePickerStart.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                startDateChanged(year, monthOfYear, dayOfMonth)
            }

            datePickerEnd.updateDate(
                end.get(Calendar.YEAR),
                end.get(Calendar.MONTH),
                end.get(Calendar.DAY_OF_MONTH)
            )
            datePickerEnd.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                setEndDateChanged(year, monthOfYear, dayOfMonth)
            }
        }
    }

    private fun startDateChanged(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, monthOfYear, dayOfMonth)
        viewModel.setStartDate(cal)
    }

    private fun setEndDateChanged(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, monthOfYear, dayOfMonth)
        viewModel.setEndDate(cal)
    }

    private fun addPeriod() {
        viewModel.addPeriod()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


