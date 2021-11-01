package de.writer_chris.babittmealplaner.ui.dish.fragments

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
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishEdit
import de.writer_chris.babittmealplaner.databinding.FragmentDishBinding
import de.writer_chris.babittmealplaner.ui.dish.adapters.DishListAdapter
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishViewModel
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishViewModelFactory

class DishFragment : Fragment() {

    //TODO story III
    //TODO add a searchbar


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

        navigationArgs?.mealId?.let {
            mealId = it
        }

        val adapter = DishListAdapter({
            val action = DishFragmentDirections.actionNavigationDishToDishDetailFragment(
                getString(R.string.details_dish),
                it.dishId, mealId
            )
            this.findNavController().navigate(action)
        }, {
            val args = ArgsToDishEdit(getString(R.string.edit_dish), it.dishId, null, null, null)
            val action = DishFragmentDirections.actionNavigationDishToEditDishFragment(args)
            this.findNavController().navigate(action)
        })


        binding.dishRecyclerView.adapter = adapter
        viewModel.allDishes.observe(this.viewLifecycleOwner) { dishes ->
            dishes.let {
                adapter.submitList(it)
            }
        }
        binding.dishRecyclerView.layoutManager = LinearLayoutManager(this.context)
        if (mealId == -1) {
            binding.btnAddDish.apply {
                setOnClickListener {
                    val args = ArgsToDishEdit(getString(R.string.add_dish), -1, null, null, null)
                    val action =
                        DishFragmentDirections.actionNavigationDishToEditDishFragment(args)
                    this.findNavController().navigate(action)
                }
            }
        } else {
            binding.btnAddDish.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}