package com.ahseed.veta.user.modelClass

data class Profile(
    val id:String,
    val name:String,
    val phoneNumber:String,
    val email:String,
    val profession:String,
    val profileUrl:String,
    val joinedDate:String,
    val courseHours:Int
)
