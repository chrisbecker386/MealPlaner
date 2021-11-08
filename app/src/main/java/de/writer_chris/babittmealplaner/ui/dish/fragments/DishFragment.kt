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
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishDetails
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishEdit
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.data.utility.TEMPORAL_FILE_NAME
import de.writer_chris.babittmealplaner.databinding.FragmentDishBinding
import de.writer_chris.babittmealplaner.ui.dish.adapters.DishListAdapter
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishViewModel
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishViewModelFactory

class DishFragment : Fragment() {

    private val viewModel: DishViewModel by viewModels {
        DishViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentDishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = DishListAdapter(
            {
                val args = ArgsToDishDetails(
                    getString(R.string.details_dish),
                    it.dishId,
                    null
                )
                navToDishDetails(args)
            }, {
                val args = ArgsToDishEdit(
                    getString(R.string.edit_dish),
                    it.dishId,
                    it.dishName,
                    it.duration.toString(),
                    it.description
                )
                navToEditDish(args)
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
                val args = ArgsToDishEdit(getString(R.string.add_dish), -1, null, null, null)
                navToEditDish(args)
            }
        }
    }

    private fun navToEditDish(args: ArgsToDishEdit) {
        deleteTempFile()
        val action =
            DishFragmentDirections.actionDishFragmentToDishEditFragment(args)
        this.findNavController().navigate(action)
    }

    private fun navToDishDetails(args: ArgsToDishDetails) {
        val action = DishFragmentDirections.actionDishFragmentToDishDetailsFragment(args)
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //deletes a TempFile, that maybe not was erased
    private fun deleteTempFile() {
        if (DataUtil.isFileExists(requireContext(), TEMPORAL_FILE_NAME)) {
            DataUtil.deletePhotoFromInternalStorage(requireContext(), TEMPORAL_FILE_NAME)
        }
    }
}