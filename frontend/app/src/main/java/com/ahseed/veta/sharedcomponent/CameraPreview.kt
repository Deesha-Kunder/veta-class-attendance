package com.ahseed.veta.sharedcomponent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    onFaceCropped:(Bitmap)-> Unit,
    onNoFace:()-> Unit = {}
){
    val context = LocalContext.current
    val lifeCycle = LocalLifecycleOwner.current

    val previewView = remember{
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    val faceDetector = remember{
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setMinFaceSize(0.25f)
                .build()
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
            faceDetector.close()
        }
    }
    AndroidView(
        factory = {previewView},
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
    )
    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider
            .getInstance(context)
            .get()

        val preview = Preview.Builder()
            .build()
            .apply{
                setSurfaceProvider(previewView.surfaceProvider)
            }
        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            )
            .build()

        analysis.setAnalyzer(cameraExecutor){proxy ->
            val mediaImage = proxy.image
            if(mediaImage == null){
                proxy.close()
                return@setAnalyzer
            }
            val rotation = proxy.imageInfo.rotationDegrees
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                rotation
            )
            faceDetector.process(inputImage)
                .addOnSuccessListener { faces->
                    if(faces.isEmpty()){
                        onNoFace()
                    }else{
                        val face = faces.first()
                        val box = face.boundingBox

                        val bitMap = mediaImage.toBitmap(rotation)

                        val left = box.left.coerceIn(0, bitMap.width)
                        val top = box.top.coerceIn(0, bitMap.height)
                        val width = box.width().coerceAtMost(bitMap.width - left)
                        val height = box.height().coerceAtMost(bitMap.height - top)

                        if (width > 0 && height > 0) {
                            val cropped = Bitmap.createBitmap(
                                bitMap,
                                left,
                                top,
                                width,
                                height
                            )

                            onFaceCropped(cropped)
                        }
                    }

                }
                .addOnCompleteListener {
                    proxy.close()
                }
        }
        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(
            lifeCycle,
            CameraSelector.DEFAULT_FRONT_CAMERA,
            preview,
            analysis
        )
    }
}

fun Image.toBitmap(rotationDegrees: Int): Bitmap {

    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(
        nv21,
        ImageFormat.NV21,
        width,
        height,
        null
    )

    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(
        Rect(0, 0, width, height),
        100,
        out
    )

    val imageBytes = out.toByteArray()
    var bitmap = BitmapFactory.decodeByteArray(
        imageBytes,
        0,
        imageBytes.size
    )

    if (rotationDegrees != 0) {
        val matrix = Matrix().apply {
            postRotate(rotationDegrees.toFloat())
        }
        bitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    return bitmap
}

