package com.example.clothesshop.ui.type


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.data.Resource
import com.example.clothesshop.data.type.TypeRepository
import com.example.clothesshop.model.Type
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TypeViewModel @Inject constructor(private val typeRepository: TypeRepository) : ViewModel() {

    private val _typeForm = MutableLiveData<Type>()
    val typeFormState: LiveData<Type> = _typeForm

    fun getTypes(path: String) {
        viewModelScope.launch {
            typeRepository.getTypes(path).collect() { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _typeForm.value = resource.data!!
                    }
                    is Resource.Error -> {
                        //Log.w(TAG, resource.error!!)
                    }
                }

            }
        }
    }
}