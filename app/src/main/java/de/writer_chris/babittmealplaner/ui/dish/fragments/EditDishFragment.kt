package de.writer_chris.babittmealplaner.ui.dish.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishImageSelection
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.data.utility.TEMPORAL_FILE_NAME
import de.writer_chris.babittmealplaner.databinding.FragmentEditDishBinding
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishViewModel
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishViewModelFactory

class EditDishFragment : Fragment() {
    //TODO renew edit Dish
    //TODO  *** add ingredient
    //TODO  if ingredient not exists add it with a unitType
    //TODO  show a list of all ingredients

    private val viewModel: DishViewModel by viewModels {
        DishViewModelFactory(Repository(requireContext()))
    }

    private val navigationArgs: EditDishFragmentArgs by navArgs()
    private var _binding: FragmentEditDishBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                deleteTemporalImage()
                findNavController().navigate(EditDishFragmentDirections.actionEditDishFragmentToNavigationDish())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() {
        binding.apply {
            //add Dish
            if (navigationArgs.argsToDishEdit.dishId < 0) {
                btnDishSave.setOnClickListener { addNewDish() }
                btnDishDelete.visibility = View.GONE
            }
            //update Dish
            else {
                btnDishSave.setOnClickListener { updateDish() }
                btnDishDelete.visibility = View.VISIBLE
                btnDishDelete.setOnClickListener { showDeleteConfirmationDialog() }
            }

            if (navigationArgs.argsToDishEdit.name != null) {
                txtInputEditDishName.setText(
                    navigationArgs.argsToDishEdit.name,
                    TextView.BufferType.SPANNABLE
                )
            }
            if (navigationArgs.argsToDishEdit.duration != null) {
                txtInputEditDishDuration.setText(
                    navigationArgs.argsToDishEdit.duration,
                    TextView.BufferType.SPANNABLE
                )
            }
            if (navigationArgs.argsToDishEdit.description != null) {
                txtInputEditDishDescription.setText(
                    navigationArgs.argsToDishEdit.description,
                    TextView.BufferType.SPANNABLE
                )
            }

            imgViewEditDish.setOnClickListener {
                val args = ArgsToDishImageSelection(
                    navigationArgs.argsToDishEdit.title,
                    navigationArgs.argsToDishEdit.dishId,
                    getNameString(),
                    getDurationString(),
                    getDescriptionString()
                )
                val action =
                    EditDishFragmentDirections.actionEditDishFragmentToDishImageSelectorFragment(
                        args
                    )
                findNavController().navigate(action)
            }

            imageHandler()
        }
    }

    //image
    private fun imageHandler() {
        if (isImageExists(TEMPORAL_FILE_NAME)) {
            setImage(TEMPORAL_FILE_NAME)
        } else {
            if (navigationArgs.argsToDishEdit.dishId > 0) {
                if (isImageExists(navigationArgs.argsToDishEdit.dishId.toString())) {
                    setImage(navigationArgs.argsToDishEdit.dishId.toString())
                } else {
                    setDefaultImage()
                }
            } else {
                setDefaultImage()
            }
        }
    }

    private fun setImage(filename: String) {
        binding.imgViewEditDish.setImageBitmap(
            DataUtil.loadDishPictureFromInternalStorage(
                requireContext(),
                filename
            )
        )
    }

    private fun setDefaultImage() {
        binding.imgViewEditDish.setImageResource(R.drawable.ic_img_search_96)
    }

    private fun isImageExists(filename: String): Boolean {
        return DataUtil.isFileExists(requireContext(), filename)
    }

    private fun deleteTemporalImage() {
        if (DataUtil.isFileExists(requireContext(), TEMPORAL_FILE_NAME)) {
            DataUtil.deletePhotoFromInternalStorage(requireContext(), TEMPORAL_FILE_NAME)
        }
    }

    //data
    private fun addNewDish() {
        if (isEntryValid()) {
            viewModel.addDish(
                getNameString(),
                getDescriptionString(),
                getDurationLong(),
                requireContext()
            )
            val action = EditDishFragmentDirections.actionEditDishFragmentToNavigationDish()
            findNavController().navigate(action)
        }
    }

    private fun updateDish() {
        if (isEntryValid()) {
            viewModel.editDish(getParameterDish(), requireContext())
            val action = EditDishFragmentDirections.actionEditDishFragmentToNavigationDish()
            findNavController().navigate(action)
        }
    }

    private fun deleteDish() {
        viewModel.eraseDish(this.navigationArgs.argsToDishEdit.dishId, requireContext())
        val action = EditDishFragmentDirections.actionEditDishFragmentToNavigationDish()
        findNavController().navigate(action)
    }

    //dialogs
    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_dish_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ -> deleteDish() }
            .show()
    }

    private fun showIncompleteConformationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_alert_title))
            .setMessage(getString(R.string.incomplete_dish_edit_text))
            .setCancelable(false)
            .setNeutralButton(getString(R.string.ok)) { _, _ -> }
            .show()
    }

    //helper methods
    private fun getNameString(): String {
        return binding.txtInputEditDishName.text.toString()
    }

    private fun getDescriptionString(): String {
        return binding.txtInputEditDishDescription.text.toString()
    }

    private fun getDurationString(): String {
        return binding.txtInputEditDishDuration.text.toString()
    }

    private fun getDurationLong(): Long {
        return binding.txtInputEditDishDuration.text.toString().toLong()
    }

    private fun isEntryValid(): Boolean {
        return if (viewModel.isEntryValidString(getNameString()) &&
            viewModel.isEntryValidString(getDescriptionString()) &&
            viewModel.isEntryValidString(getDurationString())
        ) {
            true
        } else {
            showIncompleteConformationDialog()
            false
        }
    }

    private fun getParameterDish(): Dish {
        return Dish(
            navigationArgs.argsToDishEdit.dishId,
            getNameString(),
            getDurationLong(),
            getDescriptionString()
        )
    }

}