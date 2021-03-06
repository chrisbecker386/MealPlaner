package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

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
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDish
import de.writer_chris.babittmealplaner.data.parcels.ArgsToMealFromPeriod
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.databinding.FragmentMealDishDetailsBinding
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishDetailsViewModel
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishDetailsViewModelFactory

class MealDishDetailsFragment : Fragment() {
    lateinit var dish: Dish
    lateinit var meal: Meal
    private val navigationArgs: MealDishDetailsFragmentArgs by navArgs()

    private val viewModel: DishDetailsViewModel by viewModels {
        DishDetailsViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentMealDishDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealDishDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDish()
        setMeal()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setDish() {
        viewModel.retrieveDish(navigationArgs.args.dishId).observe(this.viewLifecycleOwner) {
            dish = it
            setDefaults(dish)
        }
    }

    private fun setMeal() {
        val mealId = navigationArgs.args.mealId ?: -1
        if (mealId > 0) {
            viewModel.retrieveMeal(mealId).observe(this.viewLifecycleOwner) {
                meal = it
                if (meal.dishId == null) {
                    setSelect()
                } else {
                    setReSelect()
                }
            }
        }
    }

    private fun setDefaults(dish: Dish) {
        binding.apply {
            btnDishReselect.visibility = View.GONE
            btnDishUnselect.visibility = View.GONE
            setImage(dish.dishId)
            txtDishName.text = dish.dishName
            txtDishDescription.text = dish.description
            txtDishDuration.text = getString(R.string.duration, dish.duration.toString())
            btnDishSetChanges.text = getString(R.string.ok)
        }
    }

    private fun setReSelect() {
        binding.apply {
            btnDishReselect.visibility = View.VISIBLE
            btnDishUnselect.visibility = View.VISIBLE
            btnDishSetChanges.text = getString(R.string.ok)
            btnDishSetChanges.setOnClickListener {
                navToMealsFromPeriod(ArgsToMealFromPeriod(meal.periodId))
            }
            btnDishUnselect.setOnClickListener {
                viewModel.updateMealWithDishId(meal, null)
                navToMealsFromPeriod(ArgsToMealFromPeriod(meal.periodId))
            }
            btnDishReselect.setOnClickListener {
                viewModel.updateMealWithDishId(meal, null)
                navToMealDish(ArgsToDish(meal.mealId))
            }
        }
    }

    private fun setSelect() {
        binding.apply {
            btnDishSetChanges.text = getString(R.string.select)
            btnDishSetChanges.setOnClickListener {
                viewModel.updateMealWithDishId(meal, dish)
                navToMealsFromPeriod(ArgsToMealFromPeriod(meal.periodId))
            }
        }
    }

    private fun setImage(dishId: Int) {
        if (DataUtil.isFileExists(requireContext(), dishId.toString())) {
            binding.imgViewDish.setImageBitmap(
                DataUtil.loadDishPictureFromInternalStorage(
                    requireContext(),
                    dishId.toString()
                )
            )
        } else {
            binding.imgViewDish.setImageResource(R.drawable.ic_broken_image_96)
        }
    }

    private fun navToMealsFromPeriod(args: ArgsToMealFromPeriod) {
        val action =
            MealDishDetailsFragmentDirections.actionMealDishDetailsFragmentToMealsFromPeriodFragment(
                args
            )
        findNavController().navigate(action)
    }

    private fun navToMealDish(args:ArgsToDish){
        val action =
            MealDishDetailsFragmentDirections.actionMealDishDetailsFragmentToMealDishListFragment(args)
        findNavController().navigate(action)
    }
}