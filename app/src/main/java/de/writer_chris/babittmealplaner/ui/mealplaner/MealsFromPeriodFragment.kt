package de.writer_chris.babittmealplaner.ui.mealplaner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.databinding.FragmentMealsFromPeriodBinding

class MealsFromPeriodFragment : Fragment() {
    lateinit var period: Period
    private val viewModel: MealsFromPeriodViewModel by viewModels {
        MealsFromPeriodViewModelFactory(Repository(requireContext()))
    }
    private val navigationArgs: MealsFromPeriodFragmentArgs by navArgs()

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

        //TODO bind recyclerView and show it
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}