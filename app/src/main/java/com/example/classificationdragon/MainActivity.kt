
package com.example.classificationdragon

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.classificationdragon.databinding.ActivityMainBinding
import com.example.classificationdragon.ui.HasilDeteksi
import com.example.classificationdragon.ui.HistoryActivity
import com.example.classificationdragon.ui.PreviewActivity
import com.example.classificationdragon.viewmodel.ClassificationViewModel
import com.example.classificationdragon.viewmodel.ClassificationViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var classificationViewModel: ClassificationViewModel


    private val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classificationViewModel = ViewModelProvider(
            this,
            ClassificationViewModelFactory(applicationContext)
        ).get(ClassificationViewModel::class.java)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvNews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        observeViewModel()

        binding.selectButton.setOnClickListener { requestPhotoVideoPermission() }
        binding.buttonCamera.setOnClickListener { openCamera() }
        binding.tombolRiwayat.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun observeViewModel() {
        classificationViewModel.classificationResult.observe(this, Observer {
            Log.d("Result", it)
        })
        classificationViewModel.predictedDisease.observe(this, Observer { disease ->
            classificationViewModel.bitmap?.let { bitmap ->
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()

                val intent = Intent(this, HasilDeteksi::class.java).apply {
                    putExtra("RESULT_TEXT", classificationViewModel.classificationResult.value)
                    putExtra("IMAGE", byteArray)
                    putExtra("DISEASE_NAME", disease)
                }
                startActivity(intent)
            }
        })
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val dimension = minOf(bitmap.width, bitmap.height)
        val xOffset = (bitmap.width - dimension) / 2
        val yOffset = (bitmap.height - dimension) / 2
        return Bitmap.createBitmap(bitmap, xOffset, yOffset, dimension, dimension)
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val squareBitmap = cropToSquare(imageBitmap)
            val savedImagePath = saveImage(squareBitmap)
            classificationViewModel.bitmap = squareBitmap
            classificationViewModel.classifyImage(squareBitmap, savedImagePath)
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

    private fun requestPhotoVideoPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            requestPermissionLauncher.launch(arrayOf(permission))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { it }) {
            openGallery()
        } else {
            Toast.makeText(this, "Izin penyimpanan ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let {
                try {
                    val inputStream = contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    val resizedBitmap = Bitmap.createScaledBitmap(bitmap!!, 324, 324, true)
                    val stream = ByteArrayOutputStream()
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()

                    val intent = Intent(this, PreviewActivity::class.java).apply {
                        putExtra("IMAGE", byteArray)
                    }
                    startActivity(intent)

                } catch (e: Exception) {
                    Log.e("ImagePicker", "Error decoding image from content resolver", e)
                }
            }
        }
    }
}
