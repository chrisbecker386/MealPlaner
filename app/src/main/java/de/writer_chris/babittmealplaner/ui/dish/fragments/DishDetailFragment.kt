package de.writer_chris.babittmealplaner.ui.dish.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.databinding.FragmentDishDetailBinding
import de.writer_chris.babittmealplaner.ui.dish.DishDetailsViewModel
import de.writer_chris.babittmealplaner.ui.dish.DishDetailsViewModelFactory

class DishDetailFragment : Fragment() {
    lateinit var dish: Dish
    lateinit var meal: Meal
    private val navigationArgs: DishDetailFragmentArgs by navArgs()


    private val viewModel: DishDetailsViewModel by viewModels {
        DishDetailsViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentDishDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDishDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealId = navigationArgs.mealId
        val dishId = navigationArgs.dishId
        viewModel.retrieveDish(dishId).observe(this.viewLifecycleOwner) { it ->
            dish = it
            bindBasic()
            if (mealId == -1) {
                bindRead()
            } else {
                viewModel.retrieveMeal(mealId).observe(this.viewLifecycleOwner) {
                    meal = it
                    if (meal.dishId == null) {
                        bindSelection()
                    } else {
                        bindEditSelection()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun bindEditSelection() {
        binding.apply {
            btnDishSetChanges.text = getString(R.string.ok)
            btnDishSetChanges.setOnClickListener {
                val action =
                    DishDetailFragmentDirections.actionDishDetailFragmentToMealsFromPeriodFragment(
                        meal.periodId
                    )
                findNavController().navigate(action)
            }
            btnDishUnselect.setOnClickListener {
                viewModel.updateMealWithDishId(meal, null)
                val action =
                    DishDetailFragmentDirections.actionDishDetailFragmentToMealsFromPeriodFragment(
                        meal.periodId
                    )
                findNavController().navigate(action)
            }
            btnDishReselect.setOnClickListener {
                viewModel.updateMealWithDishId(meal, null)
                val action = DishDetailFragmentDirections.actionDishDetailFragmentToNavigationDish(
                    meal.mealId
                )
                findNavController().navigate(action)
            }

        }
    }

    private fun bindRead() {
        binding.apply {
            btnDishReselect.visibility = View.GONE
            btnDishUnselect.visibility = View.GONE
            btnDishSetChanges.text = getString(R.string.ok)
            btnDishSetChanges.setOnClickListener {
                val action = DishDetailFragmentDirections.actionDishDetailFragmentToNavigationDish()
                findNavController().navigate(action)
            }
        }
    }

    private fun bindSelection() {
        binding.apply {
            btnDishReselect.visibility = View.GONE
            btnDishUnselect.visibility = View.GONE
            btnDishSetChanges.text = getString(R.string.select)
            btnDishSetChanges.setOnClickListener {
                viewModel.updateMealWithDishId(meal, dish)
                val action =
                    DishDetailFragmentDirections.actionDishDetailFragmentToMealsFromPeriodFragment(
                        meal.periodId
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun bindBasic() {
        binding.apply {
            txtDishName.text = dish.dishName
            txtDishDuration.text = getString(R.string.duration, dish.duration.toString())
            txtDishDescription.text = dish.description
            imgViewDish.setImageResource(R.drawable.ic_lunch)

        }
    }

}