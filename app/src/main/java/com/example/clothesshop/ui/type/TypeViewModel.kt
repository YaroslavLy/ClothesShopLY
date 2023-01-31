package com.example.clothesshop.ui.type


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.data.Resource
import com.example.clothesshop.data.Result
import com.example.clothesshop.data.TypeRepository
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.model.Type
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch



class TypeViewModel(val typeRepository: TypeRepository): ViewModel() {

    private val _typeForm = MutableLiveData<Type>()
    val typeFormState: LiveData<Type> = _typeForm

    //    init {
//        typeRepository.getTypes()
//            .onEach { resource ->
//                when (resource) {
//                    is Resource.Success -> {
//                        _typeForm.value = resource.data!!
//                    }
//                    is Resource.Error -> {
//                        //Log.w(TAG, resource.error!!)
//                    }
//                }
//            }
//            .launchIn(viewModelScope)
//    }
    fun getTypes(path: String){
        viewModelScope.launch {
            typeRepository.getTypes(path).collect(){resource ->
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