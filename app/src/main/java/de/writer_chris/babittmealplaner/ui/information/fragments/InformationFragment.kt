package de.writer_chris.babittmealplaner.ui.information.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import de.writer_chris.babittmealplaner.data.utility.AppInfo
import de.writer_chris.babittmealplaner.data.utility.PIXA_BAY_BASE_URL
import de.writer_chris.babittmealplaner.databinding.FragmentInformationBinding
import de.writer_chris.babittmealplaner.ui.information.adapters.InformationListAdapter

class InformationFragment : Fragment() {
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
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
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setListView()
        setClickListener()
    }

    private fun setListView() {
        val adapter = InformationListAdapter(AppInfo.getAppInfoList(requireContext()))
        binding.infoListView.apply {
            this.adapter = adapter
        }
    }

    private fun setClickListener() {
        binding.imgViewPixaBay.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(PIXA_BAY_BASE_URL)
            startActivity(intent)
        }
    }


}