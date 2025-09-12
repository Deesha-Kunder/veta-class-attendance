package com.ahseed.veta.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.user.modelClass.MaterialItem
import com.ahseed.veta.user.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MaterialViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _materials = MutableStateFlow<List<MaterialItem>>(emptyList())
    val materials: StateFlow<List<MaterialItem>> = _materials

    init {
        loadMaterials()
    }

    private fun loadMaterials() {
        viewModelScope.launch {
            try {
                val data = repository.getMaterials()
                _materials.value = data;
            } catch (e: Exception) {
                //handle error
            }
        }
    }
}