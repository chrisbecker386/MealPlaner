package de.writer_chris.babittmealplaner.ui.dish.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishEdit
import de.writer_chris.babittmealplaner.databinding.FragmentDishImageSelectorBinding
import de.writer_chris.babittmealplaner.ui.dish.adapters.DishImageListAdapter
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishImageSelectorViewModel
import de.writer_chris.babittmealplaner.ui.dish.viewModels.DishImageSelectorViewModelFactory
import de.writer_chris.babittmealplaner.ui.dish.viewModels.PhotoStatus

class DishImageSelectorFragment : Fragment() {
    private val viewModel: DishImageSelectorViewModel by viewModels {
        DishImageSelectorViewModelFactory(Repository(requireContext()))
    }
    private val navigationArgs: DishImageSelectorFragmentArgs by navArgs()
    private var _binding: FragmentDishImageSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDishImageSelectorBinding.inflate(inflater, container, false)
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
        val adapter = getDishImageListAdapter()
        setRecyclerView(adapter)
        initObserver(adapter)
        setListener()
    }

    private fun getDishImageListAdapter(): DishImageListAdapter {
        return DishImageListAdapter {
            val args = ArgsToDishEdit(
                navigationArgs.args.title,
                navigationArgs.args.dishId,
                navigationArgs.args.dishName,
                navigationArgs.args.duration,
                navigationArgs.args.description
            )
            navToEditDish(args)
        }
    }

    private fun setRecyclerView(adapter: DishImageListAdapter) {
        binding.recyclerViewImageSelector.adapter = adapter
    }

    //Observer
    private fun initObserver(adapter: DishImageListAdapter) {
        initObserverPhotos(adapter)
        initObserverErrorMessage()
        initObserverStatus()
    }

    private fun initObserverPhotos(adapter: DishImageListAdapter) {
        viewModel.photos.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        }
    }

    private fun initObserverErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.apply {
                    txtErrorMessage.text = it
                    txtErrorMessage.visibility = View.VISIBLE
                }
            } else {
                binding.txtErrorMessage.visibility = View.GONE
            }
        }
    }

    private fun initObserverStatus() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                PhotoStatus.ERROR -> {
                    binding.imgViewDishImageSelector.visibility = View.VISIBLE
                    binding.recyclerViewImageSelector.visibility = View.GONE
                    binding.imgViewDishImageSelector.setImageResource(R.drawable.ic_broken_internet)
                }

                PhotoStatus.LOADING -> {
                    binding.imgViewDishImageSelector.visibility = View.VISIBLE
                    binding.recyclerViewImageSelector.visibility = View.GONE
                    binding.imgViewDishImageSelector.setImageResource(R.drawable.loading_animation)
                }
                PhotoStatus.DONE -> {
                    binding.recyclerViewImageSelector.visibility = View.VISIBLE
                    binding.imgViewDishImageSelector.visibility = View.GONE
                }
            }
        }
    }

    //Listner
    private fun setListener() {
        setBtnSearchListener()
        setActionSearchListener()
    }

    private fun setBtnSearchListener() {
        binding.btnDishSearch.setOnClickListener {
            if (!isTextInputEmpty()) search()
        }
    }

    private fun setActionSearchListener() {
        val txtInput = binding.txtInputDishImageSelectorSearch
        txtInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!isTextInputEmpty()) {
                    search()
                }
            }
            return@setOnEditorActionListener true
        }
    }

    //Helper Methods
    private fun search() {
        if (!isInternetAvailable()) {
            viewModel.setNoInternetConnectivity()
            return
        }
        viewModel.setHasInternetConnectivity()
        val inputText = binding.txtInputDishImageSelectorSearch.text
        if (isInputValid(inputText.toString())) {
            viewModel.search(inputText.toString())
            hideSoftKeyboard()
        }
    }

    private fun isTextInputEmpty(): Boolean {
        val inputText = binding.txtInputDishImageSelectorSearch.text.toString()
        return inputText.isBlank()
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

    private fun isInternetAvailable(): Boolean {
        var result: Boolean
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return result
    }

    private fun hideSoftKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun showInformationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.warning))
            .setMessage(getString(R.string.forbidden_symbols_text))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .show()
    }

    private fun navToEditDish(args: ArgsToDishEdit) {
        val action =
            DishImageSelectorFragmentDirections.actionDishImageSelectorFragmentToDishEditFragment(
                args
            )
        this.findNavController().navigate(action)
    }
}