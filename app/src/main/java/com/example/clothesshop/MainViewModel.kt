package com.example.clothesshop

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.model.Type
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _categoryForm = MutableLiveData<String>()
    val сategoryFormState: LiveData<String> = _categoryForm

    fun setCategory(category: String){
        viewModelScope.launch {
            _categoryForm.value= category
        }

    }



}