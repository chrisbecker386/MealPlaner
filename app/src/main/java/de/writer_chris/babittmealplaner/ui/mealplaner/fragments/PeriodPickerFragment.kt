package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.annotation.SuppressLint
import android.content.res.Resources
import android.hardware.SensorAdditionalInfo
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.FragmentPeriodPickerBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.PeriodPickerViewModel
import de.writer_chris.babittmealplaner.ui.mealplaner.PeriodPickerViewModelFactory

enum class PeriodState { START_DATE, END_DATE, SUMMARY }

const val MAX_DAYS = 24

class PeriodPickerFragment : Fragment() {
    private var periodPickerState = MutableLiveData(PeriodState.START_DATE)

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
        //Disabling Year View
        val year = binding.datePicker.findViewById<View>(
            Resources.getSystem().getIdentifier("android:id/year", null, null)
        )
        year.visibility = View.GONE
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
            PeriodState.START_DATE -> {
                setupStart()
            }
            PeriodState.END_DATE -> {
                setupEnd()
            }
            else -> {
                setupSummary()
            }
        }
    }

    private fun setupStart() {
        viewModel.startDate.observe(this.viewLifecycleOwner) {
            it.let {
                binding.txtWeekday.setText(CalendarUtil.calendarToWeekdayResId(it))
            }
        }

        binding.apply {
            txtInfo.text = getString(R.string.start_date)

            val start =
                viewModel.startDate.value ?: throw IllegalArgumentException("startDate is null")

            datePickerMinMaxDate(start, 24)

            datePicker.updateDate(
                start.get(Calendar.YEAR),
                start.get(Calendar.MONTH),
                start.get(Calendar.DAY_OF_MONTH)
            )



            datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                startDateChanged(year, monthOfYear, dayOfMonth)
            }

            btnSetChanges.text = getString(R.string.start_date)

            btnSetChanges.setOnClickListener {
                datePicker.setOnDateChangedListener { _, _, _, _ -> }
                periodPickerState.value = PeriodState.END_DATE

            }


        }
    }

    private fun setupEnd() {
        val start =
            viewModel.startDate?.value ?: throw IllegalArgumentException("startDate is null")

        viewModel.endDate.observe(this.viewLifecycleOwner) {
            it.let {
                binding.txtWeekday.setText(CalendarUtil.calendarToWeekdayResId(it))

                binding.txtPeriodOfTime.text =
                    "From ${CalendarUtil.longToGermanDate(start.timeInMillis)} till ${
                        CalendarUtil.longToGermanDate(it.timeInMillis)
                    }"
            }
        }

        binding.apply {
            txtInfo.text = getString(R.string.end_date)

            val end =
                viewModel.endDate.value ?: throw IllegalArgumentException("endDate is null")

            datePickerMinMaxDate(start, 24)

            datePicker.updateDate(
                end.get(Calendar.YEAR),
                end.get(Calendar.MONTH),
                end.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                setEndDateChanged(year, monthOfYear, dayOfMonth)
                Log.d("date2:", "year:$year, month: $monthOfYear, dayOfMonth: $dayOfMonth")
            }

            btnSetChanges.text = getString(R.string.set_end_date)

            btnSetChanges.setOnClickListener {
                datePicker.setOnDateChangedListener { _, _, _, _ -> }
                periodPickerState.value = PeriodState.SUMMARY
            }
        }
    }


    private fun setupSummary() {

        binding.apply {
            txtInfo.text = getString(R.string.summary)
            val start = viewModel.startDate.value
                ?: throw IllegalArgumentException("PeriodPickerFragment shit happens")
            val end = viewModel.endDate.value
                ?: throw IllegalArgumentException("PeriodPickerFragment shit happens")

            txtPeriodOfTime.text =
                "From ${CalendarUtil.longToGermanDate(start.timeInMillis)} till ${
                    CalendarUtil.longToGermanDate(end.timeInMillis)
                }"

            datePicker.visibility = View.GONE

            txtPeriodOfTime.text
            btnSetChanges.text = getString(R.string.save)
            btnSetChanges.setOnClickListener {
                addPeriod()
                val action =
                    PeriodPickerFragmentDirections.actionDatePickerFragmentToNavigationMeal()
                this@PeriodPickerFragment.findNavController().navigate(action)
            }
        }

    }

    private fun datePickerMinMaxDate(minDate: Calendar, maxAdditionalDays: Int) {
        val tempDate = Calendar.getInstance()
        tempDate.timeInMillis = minDate.timeInMillis
        tempDate.add(Calendar.DATE, maxAdditionalDays)
        binding.datePicker.maxDate = tempDate.timeInMillis
        binding.datePicker.minDate = minDate.timeInMillis
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


