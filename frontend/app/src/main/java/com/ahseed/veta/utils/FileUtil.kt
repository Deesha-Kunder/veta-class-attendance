package com.ahseed.veta.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import androidx.documentfile.provider.DocumentFile
import androidx.core.net.toUri

object FileUtil {
    fun uriToMultiPart(context: Context, uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalFileName = getFileNameFromUri(context, uri)
        val file = File(context.cacheDir, originalFileName)
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        val fileRequest = file.asRequestBody("application/pdf".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, fileRequest)
        return body;
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String {
        val documentFile = DocumentFile.fromSingleUri(context, uri)
        return documentFile?.name ?: "my_document.pdf"
    }

    fun downloadFile(context: Context, fileUrl:String,fileName:String){
        try{
            val request = DownloadManager.Request(fileUrl.toUri())
                .setTitle(fileName)
                .setDescription("Downloading file")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            Log.d("Download","Downloaded successfully $fileName")
        }catch (e: Exception){
            Log.e("Download","Error downloading file ${e.message}")
        }

    }
}