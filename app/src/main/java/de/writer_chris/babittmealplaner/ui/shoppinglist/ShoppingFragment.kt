package de.writer_chris.babittmealplaner.ui.shoppinglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.databinding.FragmentShoppingBinding


class ShoppingFragment : Fragment() {
    private lateinit var shoppingViewModel: ShoppingViewModel
    private var _binding: FragmentShoppingBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shoppingViewModel = ViewModelProvider(this).get(ShoppingViewModel::class.java)
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root = binding.root
        val textView: TextView = binding.textShopping

        shoppingViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}