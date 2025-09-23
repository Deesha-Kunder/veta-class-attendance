package com.ahseed.veta.screen.student.modelClass

data class StudentProfile(
    val id:String,
    val name:String,
    val phoneNumber:String,
    val email:String,
    val profession:String,
    val profileUrl:String,
    val joinedDate:String,
    val courseHours:Int
)data class AdminProfile(
    val id:String,
    val name:String,
    val phoneNumber:String,
    val email:String,
    val profileUrl:String
)

