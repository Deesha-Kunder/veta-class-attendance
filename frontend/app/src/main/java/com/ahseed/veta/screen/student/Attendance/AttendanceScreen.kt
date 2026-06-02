package com.ahseed.veta.screen.student.Attendance

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahseed.veta.data.modelclass.FaceRecognizeRequest
import com.ahseed.veta.sharedcomponent.CameraPreview
import com.ahseed.veta.sharedcomponent.FaceNetHelper
import com.ahseed.veta.sharedcomponent.RequestCameraPermission
import com.ahseed.veta.ui.theme.Purple40
import com.ahseed.veta.ui.theme.PurpleGrey40
import com.ahseed.veta.ui.theme.primary
import com.ahseed.veta.utils.formatDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MarkAttendance(
    navController : NavController,
    viewModel: FaceViewModel = hiltViewModel()
) {
    var startCamera by remember { mutableStateOf(false) }
    var capturedBitMap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val scope  = rememberCoroutineScope()
    val faceNetHelper = remember { FaceNetHelper(context) }
    val isLoading by viewModel.isLoading.collectAsState()
    var isProcessing by remember { mutableStateOf(false) }
    val message by viewModel.message.collectAsState()
    val faceRecognizeResponse by viewModel.faceRecognizeResponse.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getStudentStatus()
    }


    val status by viewModel.status.collectAsState()

    if(isLoading && status == null){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Attendance",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = {
                    navController.navigate("registerFaceScreen")
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.PersonAdd,
                    contentDescription = "Registration"
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Purple40),
            contentAlignment = Alignment.Center
        ) {
            when{
                isLoading->{
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
                startCamera->{
                    RequestCameraPermission {
                        CameraPreview(
                            onFaceCropped ={ bitmap->
                                capturedBitMap = bitmap
                            },
                            onNoFace = {
                                capturedBitMap = null
                            }
                        )
                    }
                }
                else->{
                    val canOpenCamera = status?.faceRegistered == true && status?.checkOut != true
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = "camera",
                        modifier = Modifier
                            .size(70.dp)
                            .clickable(enabled = canOpenCamera) {
                                startCamera = true
                            },
                        tint = if(canOpenCamera) Color.Black else Color.Gray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        val displayMessage = when{
            status?.faceRegistered == false-> "Please register your face first"
            status?.checkOut == true -> "Exit already recorded"
            status?.checkIn == true -> "Entry already recorded"
            message != null -> message!!
            else->"Align your face within frame"
        }
        Text(
            text = displayMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        status?.checkInTime?.let {
            Text(
                text = "Entry Time: ${formatDateTime(it)}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
        status?.checkOutTime?.let {
            Text(
                text = "Exit Time: ${formatDateTime(it)}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val bitmap = capturedBitMap?:return@Button
                if(isProcessing || isLoading) return@Button
                isProcessing = true

                scope.launch {
                    val embedding = withContext(Dispatchers.IO){
                        faceNetHelper.getEmbedding(bitmap)
                    }
                    viewModel.recognizeFace(
                        FaceRecognizeRequest(embedding.toList())
                    )
                    isProcessing = false
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = ButtonDefaults.buttonColors(containerColor = primary),
            enabled = capturedBitMap != null && !isLoading
        )
        {
            Text(
                text = "Mark Attendance",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}