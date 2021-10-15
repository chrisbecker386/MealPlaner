package de.writer_chris.babittmealplaner.ui.mealplaner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.databinding.FragmentMealsFromPeriodBinding

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
        val adapter = DayMealsListAdapter {
            Toast.makeText(this.context, "${it.date}", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewMealsFromPeriod.adapter = adapter
        viewModel.meals.observe(this.viewLifecycleOwner) {
            adapter.submitList(viewModel.getDayMeals())
        }
        binding.recyclerViewMealsFromPeriod.layoutManager = LinearLayoutManager(this.context)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}