package com.example.classificationdragon

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.example.classificationdragon.models.HistoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Klasifikasi(private val context: Context) {
    private val interpreter: Interpreter by lazy { Interpreter(loadModelFile()) }
//    private val labels = listOf("Antraknosa", "Bercak Merah", "Busuk Batang", "Kudis", "Mosaik")
private val labels = listOf("Antraknosa", "Busuk Batang", "Kudis", "Mosaik")

    interface ClassificationCallback {
        fun onResult(resultText: String, predictedDisease: String)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(224 * 224)
        bitmap.getPixels(intValues, 0, 224, 0, 0, 224, 224)

        var pixelIndex = 0
        for (i in 0 until 224) {
            for (j in 0 until 224) {
                val pixelValue = intValues[pixelIndex++]
                byteBuffer.putFloat((pixelValue shr 16 and 0xFF).toFloat()) // R
                byteBuffer.putFloat((pixelValue shr 8 and 0xFF).toFloat())  // G
                byteBuffer.putFloat((pixelValue and 0xFF).toFloat())       // B
            }
        }
        return byteBuffer
    }

    fun classifyImage(bitmap: Bitmap, imagePath: String, callback: ClassificationCallback) {
        try {
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)
            val result = Array(1) { FloatArray(labels.size) }

            interpreter.run(byteBuffer, result)

            val maxIndex = result[0].indices.maxByOrNull { result[0][it] } ?: -1
            val confidence = result[0][maxIndex] * 100
            val threshold = 70.0f // batas kepercayaan minimal

            val predictedDisease = if (confidence < threshold) {
                "Tidak Diketahui"
            } else {
                labels[maxIndex]
            }

            val resultText = buildString {
                append("Hasil: $predictedDisease (${confidence.toInt()}%)\n\n")
                append("Confidence per class:\n")
                labels.forEachIndexed { index, label ->
                    append("$label: ${(result[0][index] * 100).toInt()}%\n")
                }
            }

            Log.d("ClassificationResult", resultText)

            // Simpan ke database (jika bukan 'Tidak Diketahui')
            if (predictedDisease != "Tidak Diketahui") {
                saveClassificationHistory(predictedDisease, imagePath)
            }

            callback.onResult(resultText, predictedDisease)

        } catch (e: Exception) {
            Log.e("Classification", "Error during classification", e)
        }
    }


    private fun loadModelFile(): MappedByteBuffer {
        return try {
            val assetFileDescriptor = context.assets.openFd("Klasifikasi_EfficientNetB0.tflite")
            val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel: FileChannel = fileInputStream.channel
            fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
        } catch (e: Exception) {
            Log.e("ModelLoading", "Error loading model", e)
            throw RuntimeException("Gagal memuat model TFLite.")
        }
    }


    private fun saveClassificationHistory(diseaseName: String, imagePath: String) {
        try {
            val db = HistoriDatabase.getDatabase(context)
            val dao = db.historyDao()

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val history = HistoryEntity(diseaseName = diseaseName, date = currentDate, imagePath = imagePath)

            CoroutineScope(Dispatchers.IO).launch {
                dao.insertHistory(history)
                Log.d("Database", "Data berhasil disimpan: $diseaseName, $imagePath")
            }
        } catch (e: Exception) {
            Log.e("DatabaseError", "Gagal menyimpan data ke database", e)
        }
    }
}
