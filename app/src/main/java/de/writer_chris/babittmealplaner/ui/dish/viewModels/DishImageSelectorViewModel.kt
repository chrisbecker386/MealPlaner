package de.writer_chris.babittmealplaner.ui.dish.viewModels

import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.network.DishPhoto
import de.writer_chris.babittmealplaner.network.PixaBayApi
import kotlinx.coroutines.launch

enum class PhotoStatus { LOADING, ERROR, DONE }
class DishImageSelectorViewModel(repository: Repository) : ViewModel() {

    private val _status = MutableLiveData(PhotoStatus.DONE)
    val status: LiveData<PhotoStatus> get() = _status

    private val _photos = MutableLiveData<List<DishPhoto>>(listOf())
    val photos: LiveData<List<DishPhoto>> get() = _photos

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    fun search(searchWord: String) {
        getDishPhotos(searchWord)
    }

    private fun getDishPhotos(searchWord: String) {
        viewModelScope.launch {
            _status.value = PhotoStatus.LOADING
            try {
                _photos.value = PixaBayApi.retrofitService.searchForImage(searchWord).hits
                _status.value = PhotoStatus.DONE
                _errorMessage.value = ""
            } catch (e: Exception) {
                _status.value = PhotoStatus.ERROR
                _photos.value = listOf()
                _errorMessage.value = e.message
            }
        }
    }


}

class DishImageSelectorViewModelFactory(
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishImageSelectorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DishImageSelectorViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}