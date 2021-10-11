package de.writer_chris.babittmealplaner.ui.mealplaner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.databinding.FragmentMealBinding

class MealFragment : Fragment() {

    private val viewModel: PeriodViewModel by viewModels {
        PeriodViewModelFactory(Repository(requireContext()))
    }
    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PeriodListAdapter {
            //TODO add direction to edit Period
            /* val action = direction....
            ...
            this.findNavController().navigate(action)
            * */

        }
        binding.periodRecyclerView.adapter = adapter
        viewModel.periods.observe(this.viewLifecycleOwner) { periods ->
            periods.let { adapter.submitList(it) }
        }
        binding.periodRecyclerView.layoutManager = LinearLayoutManager(this.context)


        binding.btnAddSchedulePeriod.apply {
            setOnClickListener {
                val action = MealFragmentDirections.actionNavigationMealToDatePickerFragment()
                this.findNavController().navigate(action)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}