package com.example.classificationdragon
import android.app.Activity
import android.content.Intent
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import com.example.classificationdragon.models.HistoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IzinAkses : AppCompatActivity() {
    private val STORAGE_REQUEST_CODE = 102

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openGallery()
                } else {
                    Toast.makeText(this, "Izin penyimpanan ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Cek apakah izin sudah diberikan
    private fun checkPhotoVideoPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else { // Android 12 ke bawah
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPhotoVideoPermission() {
        if (checkPhotoVideoPermission()) {
//            openGallery()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
                )
            } else { // Android 12 ke bawah
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            }
        }
    }


    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Izin Diperlukan")
            .setMessage("Aplikasi memerlukan izin akses foto dan video untuk dapat memilih gambar.")
            .setPositiveButton("Izinkan") { _, _ ->
                requestPhotoVideoPermission()
            }
            .setNegativeButton("Tolak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Izin Diblokir")
            .setMessage("Anda telah menolak izin secara permanen. Silakan aktifkan izin di Pengaturan.")
            .setPositiveButton("Buka Pengaturan") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }



    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                permissions[Manifest.permission.READ_MEDIA_VIDEO] == true ||
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true

        if (granted) {
//            openGallery()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) ||
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_VIDEO) ||
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Jika pengguna menolak izin, beri tahu dengan dialog
                showPermissionRationale()
            } else {
                // Jika pengguna memilih "Jangan Tanya Lagi", arahkan ke Pengaturan
                showSettingsDialog()
            }
        }
    }

}

