package com.ahseed.veta.screen.admin.announcementscreen

import com.ahseed.veta.data.modelclass.MaterialItem
import com.ahseed.veta.data.modelclass.UploadedResponse

sealed class UploadState{
    object Idle:UploadState()
    object Loading: UploadState()
    data class Success(val response: UploadedResponse): UploadState()
    data class Error(val message:String): UploadState()
}

sealed class FileState{
    object Idle:FileState()
    object Loading: FileState()
    data class Success(val files: List<MaterialItem>): FileState()
    data class Error(val message:String): FileState()
}