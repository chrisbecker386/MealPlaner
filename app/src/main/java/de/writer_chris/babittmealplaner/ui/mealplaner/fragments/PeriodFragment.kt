package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.parcels.ArgsToMealFromPeriod
import de.writer_chris.babittmealplaner.data.parcels.ArgsToPeriodEdit
import de.writer_chris.babittmealplaner.databinding.FragmentPeriodBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.PeriodViewModel
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.PeriodViewModelFactory
import de.writer_chris.babittmealplaner.ui.mealplaner.adapters.PeriodListAdapter

class PeriodFragment : Fragment() {

    private val viewModel: PeriodViewModel by viewModels {
        PeriodViewModelFactory(Repository(requireContext()))
    }
    private var _binding: FragmentPeriodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeriodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() {
        val adapter = getPeriodAdapter()
        setRecyclerView(adapter)
        initObserver(adapter)
        setBtnAdd()
    }

    private fun getPeriodAdapter(): PeriodListAdapter {
        //defines onClick and onLongClick for item
        return PeriodListAdapter({
            val action =
                PeriodFragmentDirections.actionPeriodFragmentToMealsFromPeriodFragment(
                    ArgsToMealFromPeriod(it.periodId)
                )
//                actionNavigationPeriodToMealsFromPeriodFragment(it.periodId)
            this.findNavController().navigate(action)
        }, {
            val action = PeriodFragmentDirections.actionPeriodFragmentToEditPeriodFragment(
//            actionNavigationPeriodToDatePickerFragment(
                ArgsToPeriodEdit(
                    getString(R.string.update_period),
                    it.periodId
                )
            )
            this.findNavController().navigate(action)
        })
    }

    private fun setRecyclerView(adapter: PeriodListAdapter) {
        binding.apply {
            periodRecyclerView.adapter = adapter
            periodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setBtnAdd() {
        binding.btnAddSchedulePeriod.setOnClickListener {
            val action = PeriodFragmentDirections.actionPeriodFragmentToEditPeriodFragment(
//            actionNavigationPeriodToDatePickerFragment(
                ArgsToPeriodEdit(getString(R.string.add_period), -1)
            )
            findNavController().navigate(action)
        }
    }

    private fun initObserver(adapter: PeriodListAdapter) {
        viewModel.periods.observe(this.viewLifecycleOwner) { periods ->
            periods.let { adapter.submitList(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}