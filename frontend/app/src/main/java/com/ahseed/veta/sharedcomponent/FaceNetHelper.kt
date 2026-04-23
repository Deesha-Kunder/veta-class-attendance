package com.ahseed.veta.sharedcomponent

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

class FaceNetHelper(context: Context) {
    private val interpreter: Interpreter
    private val imageProcessor: ImageProcessor

    init {
        val model = FileUtil.loadMappedFile(context, "mobilefacenet.tflite")
        interpreter = Interpreter(model)

        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(112,112, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(127.5f, 127.5f))
            .build()
    }

    fun getEmbedding(bitMap: Bitmap): FloatArray{
        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitMap)
        tensorImage = imageProcessor.process(tensorImage)

        val outputShape = interpreter.getOutputTensor(0).shape()
        println("Output shape: ${outputShape.contentToString()}")


        val output = Array(1){
            FloatArray(128)
        }

        interpreter.run(tensorImage.buffer,output)

        return output[0]
    }
    fun close(){
        interpreter.close()
    }
}