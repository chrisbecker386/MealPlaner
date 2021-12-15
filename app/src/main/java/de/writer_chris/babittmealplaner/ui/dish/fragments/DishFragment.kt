package de.writer_chris.babittmealplaner.ui.dish.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishDetails
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishEdit
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.data.utility.FileName.*
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
        bind()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun bind() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
        }
        val adapter = getDishListAdapter()
        setRecyclerView(adapter)
        initObserver(adapter)
        btnAddDishListener()
    }

    private fun getDishListAdapter(): DishListAdapter {
        return DishListAdapter(
            requireContext(),
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
    }

    private fun setRecyclerView(adapter: DishListAdapter) {
        binding.dishRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun initObserver(adapter: DishListAdapter) {
        viewModel.allDishes.observe(this.viewLifecycleOwner) { dishes ->
            dishes.let {
                adapter.submitList(it)
            }
        }
    }

    private fun btnAddDishListener() {
        binding.btnAddDish.setOnClickListener {
            val args = ArgsToDishEdit(getString(R.string.add_dish), -1, null, null, null)
            navToEditDish(args)
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

    private fun deleteTempFile() {
        if (DataUtil.isFileExists(requireContext(), TEMPORAL_NAME.fileString)) {
            DataUtil.deletePhotoFromInternalStorage(requireContext(), TEMPORAL_NAME.fileString)
        }
    }
}