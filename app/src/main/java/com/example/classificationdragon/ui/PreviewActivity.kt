package com.example.classificationdragon.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.classificationdragon.data.models.Klasifikasi
import com.example.classificationdragon.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class PreviewActivity : AppCompatActivity() {
    val klasifikasi = Klasifikasi(this)
    private lateinit var imageView: ImageView
    private lateinit var cekHasilButton: Button
    private lateinit var bitmap: Bitmap  // simpan bitmap di sini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview)

        imageView = findViewById(R.id.imageViewPreview)
        cekHasilButton = findViewById(R.id.buttonCekHasil)

        val imageByteArray = intent.getByteArrayExtra("IMAGE")

        imageByteArray?.let {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            imageView.setImageBitmap(bitmap)
        }

        cekHasilButton.setOnClickListener {
            val savedImagePath = saveImage(bitmap)

            klasifikasi.classifyImage(bitmap, savedImagePath, object :
                Klasifikasi.ClassificationCallback {
                override fun onResult(resultText: String, predictedDisease: String) {
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()

                    val intent = Intent(this@PreviewActivity, HasilDeteksi::class.java).apply {
                        putExtra("RESULT_TEXT", resultText)
                        putExtra("IMAGE", byteArray)
                        putExtra("DISEASE_NAME", predictedDisease)
                    }
                    startActivity(intent)
                }
            })
        }

    }

    private fun saveImage(bitmap: Bitmap): String {
        val directory = File(getExternalFilesDir(null), "images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val filename = "IMG_${System.currentTimeMillis()}.png"
        val file = File(directory, filename)
        var outputStream: OutputStream? = null

        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
        } catch (e: Exception) {
            Log.e("SaveImage", "Gagal menyimpan gambar", e)
        } finally {
            outputStream?.close()
        }

        return file.absolutePath
    }
}
