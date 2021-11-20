package de.writer_chris.babittmealplaner.ui.mealplaner.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDish
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDishDetails
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.data.utility.PaperType
import de.writer_chris.babittmealplaner.data.utility.PdfMaker
import de.writer_chris.babittmealplaner.databinding.FragmentMealsFromPeriodBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.MealsFromPeriodViewModel
import de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels.MealsFromPeriodViewModelFactory
import de.writer_chris.babittmealplaner.ui.mealplaner.adapters.DayMealsListAdapter
import de.writer_chris.babittmealplaner.ui.mealplaner.adapters.PdfListAdapter
import de.writer_chris.babittmealplaner.ui.mealplaner.models.DayMealsAndDish


class MealsFromPeriodFragment : Fragment() {
    private lateinit var listView: ListView
    private val navigationArgs: MealsFromPeriodFragmentArgs by navArgs()
    private val viewModel: MealsFromPeriodViewModel by viewModels {
        MealsFromPeriodViewModelFactory(Repository(requireContext()), navigationArgs.args.periodId)
    }

    private var _binding: FragmentMealsFromPeriodBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealsFromPeriodBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = getDayMealListAdapter()
        setRecyclerView(adapter)
        initObserver(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.app_bar_mealplaner, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miExport -> {
                PdfMaker.createPDF(
                    null,
                    PaperType.A4,
                    requireContext(),
                    view?.findViewById(R.id.pdf_view_outer)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getDayMealListAdapter(): DayMealsListAdapter {
        return DayMealsListAdapter(requireContext(), {
            navToDishList(
                ArgsToDish(it)
            )
        }, {
            navToDishDetails(
                ArgsToDishDetails(getString(R.string.details_dish), it[0], it[1])
            )
        })
    }

    private fun setRecyclerView(adapter: DayMealsListAdapter) {
        binding.recyclerViewMealsFromPeriod.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun initObserver(adapter: DayMealsListAdapter) {
        viewModel.mealsAndDishes.observe(this.viewLifecycleOwner) { mealsAndDishes ->
            mealsAndDishes.let {
                adapter.submitList(viewModel.getDayMealsAndDish())
                setPdfData(viewModel.getDayMealsAndDish())
            }
        }
    }

    private fun setPdfData(dayMealsAndDish: List<DayMealsAndDish>) {
        binding.pdfViewTimePeriod.text = getTimePeriodString(dayMealsAndDish)
        binding.pdfViewListView.adapter = PdfListAdapter(dayMealsAndDish)
    }

    private fun navToDishList(args: ArgsToDish) {
        val action =
            MealsFromPeriodFragmentDirections.actionMealsFromPeriodFragmentToMealDishListFragment(
                args
            )
        findNavController().navigate(action)
    }

    private fun navToDishDetails(args: ArgsToDishDetails) {
        val action =
            MealsFromPeriodFragmentDirections.actionMealsFromPeriodFragmentToMealDishDetailsFragment(
                args
            )
        findNavController().navigate(action)
    }

    private fun getTimePeriodString(period: List<DayMealsAndDish>): String {
        return CalendarUtil.longToDate(period.get(0).date) + " - " + CalendarUtil.longToDate(
            period.last().date
        )
    }
}