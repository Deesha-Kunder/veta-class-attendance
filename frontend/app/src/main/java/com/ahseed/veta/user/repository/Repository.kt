package com.ahseed.veta.user.repository

import com.ahseed.veta.user.modelClass.MaterialItem
import javax.inject.Inject

class Repository @Inject constructor() {

    fun getMaterials(): List<MaterialItem>{
        val materials =  listOf(MaterialItem(1,"grammer materials","","04-05-2005"),
            MaterialItem(1,"grammer materials","","04-05-2005"),
            MaterialItem(1,"grammer materials","","04-05-2005"),
            MaterialItem(1,"grammer materials","","04-05-2005"))
        return materials;
    }
}