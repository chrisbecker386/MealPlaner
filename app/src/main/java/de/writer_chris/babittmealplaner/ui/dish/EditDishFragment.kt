package de.writer_chris.babittmealplaner.ui.dish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.writer_chris.babittmealplaner.BabittMealPlanerApplication
import de.writer_chris.babittmealplaner.data.Dish
import de.writer_chris.babittmealplaner.databinding.FragmentEditDishBinding

class EditDishFragment : Fragment() {
    lateinit var dish: Dish
    private val viewModel: DishViewModel by activityViewModels() {
        DishViewModelFactory((activity?.application as BabittMealPlanerApplication).database.dishDao())
    }
    private val navigationArgs: EditDishFragmentArgs by navArgs()
    private var _binding: FragmentEditDishBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.dishId
        if (id > 0) {
            viewModel.retrieve(id).observe(this.viewLifecycleOwner) {
                dish = it
                bind(dish)
            }
        } else {
            binding.apply {
                btnDishSave.setOnClickListener { addNewDish() }
                btnDishDelete.isVisible = false
            }
        }
    }

    private fun bind(dish: Dish) {
        binding?.apply {
            dishName.setText(dish.dishName, TextView.BufferType.SPANNABLE)
            btnDishSave.setOnClickListener { updateDish() }
            btnDishDelete.isVisible = true
            btnDishDelete.setOnClickListener { deleteDish() }
        }
    }


    private fun isEntryValid(): Boolean {
        return binding.dishName.text.toString().isNotBlank()
    }

    private fun addNewDish() {
        if (isEntryValid()) {
            viewModel.addDish(binding.dishName.text.toString())
            val action = EditDishFragmentDirections.actionEditDishFragmentToNavigationDish()
            findNavController().navigate(action)
        }
    }

    private fun updateDish() {
        if (isEntryValid()) {
            viewModel.editDish(this.navigationArgs.dishId, this.binding.dishName.text.toString())
            val action = EditDishFragmentDirections.actionEditDishFragmentToNavigationDish()
            findNavController().navigate(action)
        }
    }


    private fun deleteDish() {
        viewModel.eraseDish(this.navigationArgs.dishId)
        val action = EditDishFragmentDirections.actionEditDishFragmentToNavigationDish()
        findNavController().navigate(action)
    }

}