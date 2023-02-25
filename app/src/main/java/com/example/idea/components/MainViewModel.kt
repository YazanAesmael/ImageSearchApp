package com.example.idea.components

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idea.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val list: MutableState<MainState> = mutableStateOf(MainState())

    fun getImageList(q:String)=viewModelScope.launch{
        list.value = MainState(isLoading = true)
        try{
            when(val result = mainRepository.getQueryItems(q)){
                is Resource.Error->{
                    list.value = MainState(error = "Something went wrong")
                }
                is Resource.Success->{
                    result.data?.hits?.let {
                        list.value = MainState(data = it)
                    }

                }
                else -> {
                    return@launch
                }
            }
        }catch (e:Exception){
            list.value = MainState(error = "Something went wrong")
        }
    }


    val interestItems = mutableStateListOf<InterestsItem>()
        .apply {
            repeat(6) {
                add(InterestsItem("", false, ""))
            }
        }

    private val selectedItems = mutableListOf<Int>()

    fun toggleSelection(index: Int) {

        val item = interestItems[index]
        val isSelected = item.isSelected

        if (isSelected) {
            interestItems[index] = item.copy(isSelected = true)
            selectedItems.remove(index)
        } else if (selectedItems.size < 3) {
            interestItems[index] = item.copy(isSelected = true)
            selectedItems.add(index)
        }
    }

}