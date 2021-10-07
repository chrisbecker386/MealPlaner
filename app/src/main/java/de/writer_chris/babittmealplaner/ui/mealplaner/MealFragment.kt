package de.writer_chris.babittmealplaner.ui.mealplaner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.databinding.FragmentMealBinding

class MealFragment : Fragment() {

    private val viewModel: MealViewModel by viewModels {
        MealViewModelFactory(Repository(requireContext()))
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
        //binding.txtDevText.text = viewModel.mealSchedule.toString()
        val adapter = MealListAdapter()
        binding.mealRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.mealRecyclerView.adapter = adapter
        adapter.submitList(viewModel.mealSchedule)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}