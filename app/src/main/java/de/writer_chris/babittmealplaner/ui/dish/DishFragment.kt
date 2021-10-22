package de.writer_chris.babittmealplaner.ui.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.databinding.FragmentDishBinding

class DishFragment : Fragment() {
    //TODO storyI
    //TODO onClick -> DishDetails check
    //TODO onLongClick -> EditDish check
    //TODO DishDetails add editButton with conformation Quote

    //TODO storyII
    //TODO receiving mealId
    //TODO onClick setting dishId on meal-entry and return to mealplaner
    //TODO onLongPress see DishDetails
    //

    //TODO story III
    //TODO add a searchbar
    //
    //TODO story IV
    //TODO add ImageFunctionality with a search for Picture in editDish

    //
    private val navigationArgs: DishFragmentArgs by navArgs()


    private val viewModel: DishViewModel by viewModels {
        DishViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentDishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mealId = -1
        navigationArgs.mealId?.let {
            mealId = it
        }

        val adapter = DishListAdapter({
            val action = DishFragmentDirections.actionNavigationDishToDishDetailFragment(
                getString(R.string.details_dish),
                it.dishId, mealId
            )
            this.findNavController().navigate(action)
        }, {
            val action = DishFragmentDirections.actionNavigationDishToEditDishFragment(
                getString(R.string.edit_dish),
                it.dishId
            )
            this.findNavController().navigate(action)
        })


        binding.dishRecyclerView.adapter = adapter
        viewModel.allDishes.observe(this.viewLifecycleOwner) { dishes ->
            dishes.let {
                adapter.submitList(it)
            }
        }
        binding.dishRecyclerView.layoutManager = LinearLayoutManager(this.context)

        binding.btnAddDish.apply {
            setOnClickListener {
                val action =
                    DishFragmentDirections.actionNavigationDishToEditDishFragment(
                        getString(R.string.add_dish),
                        -1
                    )
                this.findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}