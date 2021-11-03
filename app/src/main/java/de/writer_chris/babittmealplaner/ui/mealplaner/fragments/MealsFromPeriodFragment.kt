package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.databinding.FragmentMealsFromPeriodBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.MealsFromPeriodViewModel
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.MealsFromPeriodViewModelFactory
import de.writer_chris.babittmealplaner.ui.mealplaner.adapters.DayMealsListAdapter

class MealsFromPeriodFragment : Fragment() {
    private val navigationArgs: MealsFromPeriodFragmentArgs by navArgs()
    private val viewModel: MealsFromPeriodViewModel by viewModels {
        MealsFromPeriodViewModelFactory(Repository(requireContext()), navigationArgs.periodId)
    }

    private var _binding: FragmentMealsFromPeriodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealsFromPeriodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = getDayMealListAdapter()
        setRecyclerView(adapter)
        initObserver(adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getDayMealListAdapter(): DayMealsListAdapter {
        return DayMealsListAdapter({
            val action =
                MealsFromPeriodFragmentDirections.actionMealsFromPeriodFragmentToNavigationDish(it)
            findNavController().navigate(action)
        }, {
            val action =
                MealsFromPeriodFragmentDirections.actionMealsFromPeriodFragmentToDishDetailFragment(
                    getString(R.string.details_dish), it[0], it[1]
                )
            findNavController().navigate(action)
        })
    }

    private fun setRecyclerView(adapter: DayMealsListAdapter) {
        binding.recyclerViewMealsFromPeriod.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun initObserver(adapter: DayMealsListAdapter) {
        viewModel.mealsAndDishes.observe(this.viewLifecycleOwner) { mealsAndDishes ->
            mealsAndDishes.let { adapter.submitList(viewModel.getDayMealsAndDish()) }

        }
    }
}