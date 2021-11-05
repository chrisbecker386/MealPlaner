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

import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.databinding.FragmentDishDetailBinding
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishDetailsViewModel
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishDetailsViewModelFactory

class DishDetailsFragment : Fragment() {
    lateinit var dish: Dish
    private val navigationArgs: DishDetailsFragmentArgs by navArgs()

    private val viewModel: DishDetailsViewModel by viewModels {
        DishDetailsViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentDishDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDishDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDish()

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setDish() {
        viewModel.retrieveDish(navigationArgs.args.dishId).observe(this.viewLifecycleOwner) {
            dish = it
            setDefaults(dish)
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
            btnDishSetChanges.setOnClickListener {
                val action = DishDetailsFragmentDirections.actionDishDetailsFragmentToDishFragment()
                findNavController().navigate(action)
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
}