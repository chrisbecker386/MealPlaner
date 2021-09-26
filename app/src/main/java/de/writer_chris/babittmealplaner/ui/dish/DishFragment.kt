package de.writer_chris.babittmealplaner.ui.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import de.writer_chris.babittmealplaner.BabittMealPlanerApplication
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.databinding.FragmentDishBinding

class DishFragment : Fragment() {


    private val dishViewModel: DishViewModel by viewModels { DishViewModelFactory((activity?.application as BabittMealPlanerApplication).database.dishDao()) }
    private var _binding: FragmentDishBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDish
        dishViewModel.text.observe(viewLifecycleOwner, Observer {
            it.also { textView.text = it }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addDishButton.apply {
            setOnClickListener {
                this.findNavController().navigate(R.id.editDishFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}