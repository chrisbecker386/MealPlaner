package de.writer_chris.babittmealplaner.ui.mealplaner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.databinding.FragmentDatePickerBinding


class DatePickerFragment : Fragment() {

    private val viewModel: DatePickerViewModel by viewModels {
        DatePickerViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentDatePickerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveSchedulePeriod.apply {
            setOnClickListener {
                val action = DatePickerFragmentDirections.actionDatePickerFragmentToNavigationMeal()
                this.findNavController().navigate(action)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}