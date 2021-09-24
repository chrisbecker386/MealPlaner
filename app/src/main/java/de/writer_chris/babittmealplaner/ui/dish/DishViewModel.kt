package de.writer_chris.babittmealplaner.ui.dish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DishViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dish Fragment"
    }
    val text: LiveData<String> = _text
}