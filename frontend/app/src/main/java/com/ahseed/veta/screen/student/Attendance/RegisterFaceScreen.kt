package com.ahseed.veta.screen.student.Attendance

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahseed.veta.R
import com.ahseed.veta.sharedcomponent.CameraPreview
import com.ahseed.veta.sharedcomponent.FaceNetHelper
import com.ahseed.veta.sharedcomponent.RequestCameraPermission
import com.ahseed.veta.ui.theme.Purple40
import com.ahseed.veta.ui.theme.primary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RegisterFaceScreen(
    navController: NavController,
    viewModel: FaceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val faceNetHelper = remember { FaceNetHelper(context) }
    val scope = rememberCoroutineScope()

    var currentBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var statusMessage by remember { mutableStateOf("Position your face within frame") }

    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRegistered by viewModel.isRegistered.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "back"
                )
            }
            Text(
                text = "Register Face",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }

        RequestCameraPermission {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                if (!isRegistered) {
                    CameraPreview(
                        onFaceCropped = { bitMap ->
                            if (!isLoading) {
                                currentBitmap = bitMap
                            }
                        },
                        onNoFace = {
                            if (!isLoading) currentBitmap = null
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Purple40),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.checkbox),
                            contentDescription = null,
                            modifier = Modifier.height(50.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Purple40),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message ?: when {
                isRegistered -> "Face Registered"
                currentBitmap != null -> "Face Detected! Press register."
                else -> "Position your face within frame"
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(1f)
//                .clip(RoundedCornerShape(10.dp))
//                .background(color = Color.LightGray),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Outlined.CameraAlt,
//                contentDescription = "camera",
//                modifier = Modifier.size(70.dp)
//            )
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            text = "Position your face within the frame and ensure good lighting for accurate recognition.",
//            style = MaterialTheme.typography.bodyMedium,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.padding(16.dp)
//        )
//        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val bitMap = currentBitmap ?: return@Button
                scope.launch {
                    val embedding = withContext(Dispatchers.IO) {
                        faceNetHelper.getEmbedding(bitMap)
                    }

                    viewModel.registerFace(embedding.toList())
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary),
            enabled = currentBitmap != null && !isLoading && !isRegistered
        )
        {
            Text(
                text = "Register face",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
