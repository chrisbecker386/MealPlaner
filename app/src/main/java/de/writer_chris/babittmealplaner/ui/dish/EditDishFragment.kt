package de.writer_chris.babittmealplaner.ui.dish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.databinding.FragmentEditDishBinding

class EditDishFragment : Fragment() {
    lateinit var dish: Dish
    private val viewModel: DishViewModel by viewModels {
        DishViewModelFactory(Repository(requireContext()))
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
        binding.apply {
            dishName.setText(dish.dishName, TextView.BufferType.SPANNABLE)
            btnDishSave.setOnClickListener { updateDish() }
            btnDishDelete.isVisible = true
            btnDishDelete.setOnClickListener { showConfirmationDialog() }
        }
    }


    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(binding.dishName.text.toString())
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

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_dish_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ -> deleteDish() }
            .show()
    }

}