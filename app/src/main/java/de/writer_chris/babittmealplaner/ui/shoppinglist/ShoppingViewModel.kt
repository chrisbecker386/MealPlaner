package de.writer_chris.babittmealplaner.ui.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoppingViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Shopping Fragment"
    }
    val text: LiveData<String> = _text
}