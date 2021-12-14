package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.parcels.ArgsToMealFromPeriod
import de.writer_chris.babittmealplaner.data.parcels.ArgsToPeriodEdit
import de.writer_chris.babittmealplaner.databinding.FragmentPeriodBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.PeriodViewModel
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.PeriodViewModelFactory
import de.writer_chris.babittmealplaner.ui.mealplaner.adapters.PeriodListAdapter

class PeriodFragment : Fragment() {

    private val viewModel: PeriodViewModel by viewModels {
        PeriodViewModelFactory(Repository(requireContext()))
    }
    private var _binding: FragmentPeriodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeriodBinding.inflate(inflater, container, false)
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
        val adapter = getPeriodAdapter()
        setRecyclerView(adapter)
        initObserver(adapter)
        setBtnAdd()
    }

    private fun getPeriodAdapter(): PeriodListAdapter {
        return PeriodListAdapter({
            navToMealFromPeriod(ArgsToMealFromPeriod(it.periodId))
        }, {
            navToEditPeriod(ArgsToPeriodEdit(getString(R.string.update_period), it.periodId))
        })
    }

    private fun setRecyclerView(adapter: PeriodListAdapter) {
        binding.apply {
            periodRecyclerView.adapter = adapter
            periodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObserver(adapter: PeriodListAdapter) {
        viewModel.periods.observe(this.viewLifecycleOwner) { periods ->
            periods.let { adapter.submitList(it) }
        }
    }

    private fun setBtnAdd() {
        binding.btnAddSchedulePeriod.setOnClickListener {
            navToEditPeriod(ArgsToPeriodEdit(getString(R.string.add_period), -1))
        }
    }

    //navigation
    private fun navToMealFromPeriod(args: ArgsToMealFromPeriod) {
        val action =
            PeriodFragmentDirections.actionPeriodFragmentToMealsFromPeriodFragment(args)
        this.findNavController().navigate(action)
    }

    private fun navToEditPeriod(args: ArgsToPeriodEdit) {
        val action = PeriodFragmentDirections.actionPeriodFragmentToEditPeriodFragment(args)
        this.findNavController().navigate(action)
    }
}