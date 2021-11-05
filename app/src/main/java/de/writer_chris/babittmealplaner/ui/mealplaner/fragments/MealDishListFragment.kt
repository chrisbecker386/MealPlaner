package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishDetails
import de.writer_chris.babittmealplaner.databinding.FragmentMealDishListBinding
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishViewModel
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishViewModelFactory
import de.writer_chris.babittmealplaner.ui.mealplaner.adapters.MealDishListAdapter

class MealDishListFragment : Fragment() {

    //TODO story III
    //TODO add a searchbar
    private val navigationArgs: MealDishListFragmentArgs by navArgs()

    private val viewModel: DishViewModel by viewModels {
        DishViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentMealDishListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMealDishListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mealId = -1
        mealId = navigationArgs.args.mealId

//        navigationArgs.args?.let {
//            mealId = it.mealId
//        }

        val adapter = MealDishListAdapter {
            val action =
                MealDishListFragmentDirections.actionMealDishListFragmentToMealDishDetailsFragment(
                    ArgsToDishDetails(
                        getString(R.string.details_dish),
                        it.dishId,
                        mealId
                    )
                )
            this.findNavController().navigate(action)
        }

        binding.dishRecyclerView.adapter = adapter
        viewModel.allDishes.observe(this.viewLifecycleOwner) { dishes ->
            dishes.let {
                adapter.submitList(it)
            }
        }
        binding.dishRecyclerView.layoutManager = LinearLayoutManager(this.context)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}