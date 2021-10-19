package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.FragmentPeriodPickerBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.PeriodPickerViewModel
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.PeriodPickerViewModelFactory
import java.time.Year


enum class PeriodState { CREATE, EDIT }


class PeriodPickerFragment : Fragment() {
    private val MAX_DAYS = 30 // max days between start and end
    private var periodPickerState = MutableLiveData(PeriodState.CREATE)

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
        periodPickerState.observe(this.viewLifecycleOwner) {
            setupByPickerState()
        }
    }

    private fun setupByPickerState() = run {
        when (periodPickerState.value) {
            PeriodState.CREATE -> {
                createPeriod()
            }
            PeriodState.EDIT -> {
                editPeriod()
            }
            else -> {
                createPeriod()
            }
        }
    }

    private fun createPeriod() {
        //OBSERVERS
        viewModel.startDate.observe(this.viewLifecycleOwner) { it ->
            it.let {
                binding.apply {
                    txtStartDate.text = CalendarUtil.longToGermanDate(it.timeInMillis)
                    txtWeekdayStartDay.text = getString(CalendarUtil.calendarToWeekdayResId(it))
                }
            }
        }
        viewModel.endDate.observe(this.viewLifecycleOwner) { it ->
            it.let {
                binding.apply {
                    txtEndDate.text = CalendarUtil.longToGermanDate(it.timeInMillis)
                    txtWeekdayEndDay.text = getString(CalendarUtil.calendarToWeekdayResId(it))
                }
            }
        }
        viewModel.daysBetween.observe(this.viewLifecycleOwner) {
            it.let {
                binding.txtDaysBetween.text = resources.getQuantityString(R.plurals.days, it, it)
            }
        }
        //FUNCTIONALITY
        binding.apply {
            btnDelete.visibility = View.GONE
            btnSetChanges.text = getString(R.string.save)

            //TODO add an date picker dialog
            //also with the functionality of min max day
            txtStartDate.setOnClickListener {
                datePickerDialog(viewModel.startDate.value!!, true)
            }
            txtEndDate.setOnClickListener {
                datePickerDialog(viewModel.endDate.value!!, false)
            }
            //TODO add functionality plus minus a day
            fabPlusDay.setOnClickListener { addOneDay() }
            fabMinusDay.setOnClickListener { minusOneDay() }

            btnSetChanges.setOnClickListener {
                addPeriod()
                val action =
                    PeriodPickerFragmentDirections.actionDatePickerFragmentToNavigationMeal()
                findNavController().navigate(action)
            }
        }
    }

    private fun editPeriod() {}

    //returns an list [min, max] as date boarders
    private fun datePickerMinMaxDate(): List<Long> {
        val today = Calendar.getInstance()
        val tempDate = Calendar.getInstance()
        tempDate.add(Calendar.DATE, MAX_DAYS)
        //first min second max
        return listOf(today.timeInMillis, tempDate.timeInMillis)
    }

    private fun datePickerDialog(targetDate: Calendar, isStartDate: Boolean) {
        val c = targetDate
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val minMaxDate = datePickerMinMaxDate()

        val dpd =
            DatePickerDialog(
                this.requireActivity(),
                { _, year, monthOfYear, dayOfMonth ->
                    if (isStartDate) {
                        setStartDateChanged(year, monthOfYear, dayOfMonth)
                    } else {
                        setEndDateChanged(year, monthOfYear, dayOfMonth)
                    }
                },
                year,
                month,
                day
            )


        if (isStartDate) {
            //startDate only needs an minDate
            dpd.datePicker.minDate = minMaxDate[0]
        } else {
            //endDate depends from the start, that is why endDate has a min and max value
            dpd.datePicker.minDate = minMaxDate[0]
            dpd.datePicker.maxDate = minMaxDate[1]
        }

        dpd.show()
    }


    private fun setStartDateChanged(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, monthOfYear, dayOfMonth)
        viewModel.setStartDate(cal)
    }

    private fun setEndDateChanged(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, monthOfYear, dayOfMonth)
        viewModel.setEndDate(cal)
    }

    private fun addOneDay() {
        val start = Calendar.getInstance()
        start.timeInMillis = viewModel.startDate.value?.timeInMillis
            ?: throw IllegalArgumentException("startDate is null")

        start.add(Calendar.DAY_OF_YEAR, MAX_DAYS)
        val end = Calendar.getInstance()
        end.timeInMillis = viewModel.endDate.value?.timeInMillis
            ?: throw IllegalArgumentException("endDate is null")

        end.add(Calendar.DAY_OF_YEAR, 1)


        if (end.timeInMillis > start.timeInMillis) {
            showInformationDialog()
        } else {
            setEndDateChanged(
                end.get(Calendar.YEAR),
                end.get(Calendar.MONTH),
                end.get(Calendar.DAY_OF_MONTH)
            )
        }

    }

    private fun minusOneDay() {
        val start = Calendar.getInstance()
        start.timeInMillis = viewModel.startDate.value?.timeInMillis
            ?: throw IllegalArgumentException("startDate is null")

        val end = Calendar.getInstance()
        end.timeInMillis = viewModel.endDate.value?.timeInMillis
            ?: throw IllegalArgumentException("endDate is null")

        end.add(Calendar.DAY_OF_YEAR, -1)


        if (end.timeInMillis <= start.timeInMillis) {
            showInformationDialog()
        } else {
            setEndDateChanged(
                end.get(Calendar.YEAR),
                end.get(Calendar.MONTH),
                end.get(Calendar.DAY_OF_MONTH)
            )
        }
    }

    private fun showInformationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_inforamtion_title))
            .setMessage(getString(R.string.information_text))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .show()
    }

    private fun addPeriod() {
        viewModel.addPeriod()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


