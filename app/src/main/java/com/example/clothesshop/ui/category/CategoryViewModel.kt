package com.example.clothesshop.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.data.category.CategoryRepository
import com.example.clothesshop.data.Resource
import com.example.clothesshop.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {


    //todo #1 use state flow
    private val _categoryForm = MutableLiveData<Category>()
    val categoryFormState: LiveData<Category> = _categoryForm

    //todo move from init code add add state
    init {
        //example not use collect
        categoryRepository.getCategories()
            .onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _categoryForm.value = resource.data!!
                    }
                    is Resource.Error -> {
                        //Log.w(TAG, resource.error!!)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

}