package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.FragmentEditPeriodBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.PeriodPickerViewModel
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.PeriodPickerViewModelFactory



class EditPeriodFragment : Fragment() {
    lateinit var period: Period

    private val viewModel: PeriodPickerViewModel by viewModels {
        PeriodPickerViewModelFactory(Repository(requireContext()))
    }
    private val navigationArgs: EditPeriodFragmentArgs by navArgs()
    private var _binding: FragmentEditPeriodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPeriodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            txtStartDate.setOnClickListener {
                datePickerDialog(viewModel.startDate.value!!, true)
            }
            txtEndDate.setOnClickListener {
                datePickerDialog(viewModel.endDate.value!!, false)
            }
            fabPlusDay.setOnClickListener { addOneDay() }
            fabMinusDay.setOnClickListener { minusOneDay() }
        }

        val id = navigationArgs.periodId
        //EDIT
        if (id > 0) {
            viewModel.retrievePeriod(id).observe(this.viewLifecycleOwner) {
                period = it
                bindUpdate(period)
            }
        }
        //CREATE
        else {
            bindCreate()
        }


    }


    private fun bindCreate() {
        binding.apply {
            btnDelete.visibility = View.GONE
            btnSetChanges.text = getString(R.string.save)

            btnSetChanges.setOnClickListener {
                addPeriod()
                val action =
                    EditPeriodFragmentDirections.actionDatePickerFragmentToNavigationMeal()
                findNavController().navigate(action)
            }
        }
    }

    private fun bindUpdate(period: Period) {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = period.startDate
        setStartDateChanged(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        cal.timeInMillis = period.endDate
        setEndDateChanged(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        binding.apply {
            btnSetChanges.text = getString(R.string.update)
            btnDelete.text = getString(R.string.delete)
            btnSetChanges.setOnClickListener {
                updatePeriod()
                val action = EditPeriodFragmentDirections.actionDatePickerFragmentToNavigationMeal()
                findNavController().navigate(action)

            }
            btnDelete.setOnClickListener {
                deletePeriod()
                val action = EditPeriodFragmentDirections.actionDatePickerFragmentToNavigationMeal()
                findNavController().navigate(action)
            }
        }
    }


    private fun datePickerDialog(targetDate: Calendar, isStartDate: Boolean) {
        val c = targetDate
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

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
        val end = Calendar.getInstance()
        end.timeInMillis = viewModel.endDate.value?.timeInMillis
            ?: throw IllegalArgumentException("endDate is null")
        end.add(Calendar.DAY_OF_YEAR, 1)

        setEndDateChanged(
            end.get(Calendar.YEAR),
            end.get(Calendar.MONTH),
            end.get(Calendar.DAY_OF_MONTH)
        )

    }

    private fun minusOneDay() {
        val start = Calendar.getInstance()
        start.timeInMillis = viewModel.startDate.value?.timeInMillis
            ?: throw IllegalArgumentException("startDate is null")

        val end = Calendar.getInstance()
        end.timeInMillis = viewModel.endDate.value?.timeInMillis
            ?: throw IllegalArgumentException("endDate is null")

        end.add(Calendar.DAY_OF_YEAR, -1)


        if (end.timeInMillis < start.timeInMillis) {
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
            .setTitle(getString(R.string.dialog_information_title))
            .setMessage(getString(R.string.information_text))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .show()
    }

    private fun addPeriod() {
        viewModel.addPeriod()
    }

    private fun updatePeriod() {
        viewModel.updatePeriod(period)
    }

    private fun deletePeriod() {
        viewModel.deletePeriod(period)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


