package de.writer_chris.babittmealplaner.ui.dish.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishImageSelection
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.data.utility.FileName.*
import de.writer_chris.babittmealplaner.databinding.FragmentEditDishBinding
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishEditViewModel
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishEditViewModelFactory


class DishEditFragment : Fragment() {
    //TODO  *** add ingredient
    //TODO  if ingredient not exists add it with a unitType
    //TODO  show a list of all ingredients

    private val viewModel: DishEditViewModel by viewModels {
        DishEditViewModelFactory(Repository(requireContext()))
    }

    private val navigationArgs: DishEditFragmentArgs by navArgs()
    private var _binding: FragmentEditDishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun bind() {
        binding.apply {
            //add Dish
            if (navigationArgs.args.dishId < 0) {
                btnDishSave.setOnClickListener { addNewDish() }
                btnDishDelete.visibility = View.GONE
            }
            //update Dish
            else {
                btnDishSave.setOnClickListener { updateDish() }
                btnDishDelete.visibility = View.VISIBLE
                btnDishDelete.setOnClickListener { showDeleteConfirmationDialog() }
            }

            if (navigationArgs.args.name != null) {
                txtInputEditDishName.setText(
                    navigationArgs.args.name,
                    TextView.BufferType.SPANNABLE
                )
            }
            if (navigationArgs.args.duration != null) {
                txtInputEditDishDuration.setText(
                    navigationArgs.args.duration,
                    TextView.BufferType.SPANNABLE
                )
            }
            if (navigationArgs.args.description != null) {
                txtInputEditDishDescription.setText(
                    navigationArgs.args.description,
                    TextView.BufferType.SPANNABLE
                )
            }

            imgViewEditDish.setOnClickListener {
                val args = ArgsToDishImageSelection(
                    navigationArgs.args.title,
                    navigationArgs.args.dishId,
                    getNameString(),
                    getDurationString(),
                    getDescriptionString()
                )
                val action =
                    DishEditFragmentDirections.actionDishEditFragmentToDishImageSelectorFragment(
                        args
                    )
                findNavController().navigate(action)
            }
            imageHandler()
        }
    }

    //image
    private fun imageHandler() {
        if (isImageExists(TEMPORAL_NAME.fileString)) {
            setImage(TEMPORAL_NAME.fileString)
        } else {
            if (navigationArgs.args.dishId > 0) {
                if (isImageExists(navigationArgs.args.dishId.toString())) {
                    setImage(navigationArgs.args.dishId.toString())
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

    //data
    private fun addNewDish() {
        if (isEntryValid()) {
            viewModel.addDish(
                getNameString(),
                getDescriptionString(),
                getDurationLong(),
                requireContext()
            )
            navToDish()
        }
    }

    private fun updateDish() {
        if (isEntryValid()) {
            viewModel.editDish(getParameterDish(), requireContext())
            navToDish()
        }
    }

    private fun deleteDish() {
        viewModel.eraseDish(this.navigationArgs.args.dishId, requireContext())
        navToDish()
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
        return prepString(binding.txtInputEditDishName.text.toString())
    }

    private fun getDescriptionString(): String {
        return prepString(binding.txtInputEditDishDescription.text.toString())
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

    private fun prepString(value: String): String {
        val charArr = value.trim().toCharArray()
        charArr[0] = charArr[0].uppercaseChar()
        return String(charArr)
    }

    private fun getParameterDish(): Dish {
        return Dish(
            navigationArgs.args.dishId,
            getNameString(),
            getDurationLong(),
            getDescriptionString()
        )
    }

    private fun navToDish() {
        val action = DishEditFragmentDirections.actionDishEditFragmentToDishFragment()
        findNavController().navigate(action)
    }

}