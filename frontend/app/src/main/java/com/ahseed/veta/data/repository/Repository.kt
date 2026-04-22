package com.ahseed.veta.data.repository

import com.ahseed.veta.data.modelclass.MaterialItem
import javax.inject.Inject

class Repository @Inject constructor() {

    fun getMaterials(): List<MaterialItem>{
        val materials =  listOf(
            MaterialItem("1", "grammer materials", "", "04-05-2005", ""),
            MaterialItem("1", "grammer materials", "", "04-05-2005", ""),
            MaterialItem("1", "grammer materials", "", "04-05-2005", ""),
            MaterialItem("1", "grammer materials", "", "04-05-2005", "")
        )
        return materials;
    }
}