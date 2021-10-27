package de.writer_chris.babittmealplaner.ui.dish.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.databinding.FragmentDishImageSelectorBinding
import de.writer_chris.babittmealplaner.ui.dish.adapters.DishImageListAdapter
import de.writer_chris.babittmealplaner.ui.dish.DishImageSelectorViewModel
import de.writer_chris.babittmealplaner.ui.dish.DishImageSelectorViewModelFactory
import de.writer_chris.babittmealplaner.ui.dish.PhotoStatus

class DishImageSelectorFragment : Fragment() {


    //    private val navigationArgs: DishImageSelectorFragmentArgs by navArgs()
    private val viewModel: DishImageSelectorViewModel by viewModels {
        DishImageSelectorViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentDishImageSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDishImageSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnDishSearch.setOnClickListener {
                search()
            }
        }
        val adapter = DishImageListAdapter()
        binding.recyclerViewImageSelector.adapter = adapter
        viewModel.photos.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }

        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                PhotoStatus.ERROR -> {
                } //show errorPicture
                PhotoStatus.LOADING -> {
                } //show loading Animation
                PhotoStatus.DONE -> {
                    viewModel.photos.observe(viewLifecycleOwner) {
                        // give the pics binding.recyclerViewImageSelector
                    }
                }  //show pics
            }
        }
    }

    private fun search() {
        val inputText = binding.txtInputDishImageSelectorSearch.text
        if (inputText.isNullOrBlank()) {
            return
        }
        if (isInputValid(inputText.toString()))
            viewModel.search(inputText.toString())

    }

    private fun isInputValid(inputText: String): Boolean {
        val forbiddenSymbols: CharArray = "'\"\'$%=&^§/\\()?".toCharArray()
        val charArray: CharArray =
            inputText.toCharArray()
        charArray.forEach {
            val char = it
            forbiddenSymbols.forEach {
                if (char == it) {
                    showInformationDialog()
                    binding.txtInputDishImageSelectorSearch.text = null
                    return false
                }
            }
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showInformationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.warning))
            .setMessage(getString(R.string.forbidden_symbols_text))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .show()
    }

}