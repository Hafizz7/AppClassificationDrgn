package com.example.classificationdragon.viewmodel

import android.app.Activity
import android.content.Intent
import android.Manifest
import android.app.AlertDialog
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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classificationdragon.Klasifikasi
import com.example.classificationdragon.R
import com.example.classificationdragon.databinding.ActivityMainBinding
import com.example.classificationdragon.data.db.HistoriDatabase
import com.example.classificationdragon.viewmodel.HasilDeteksi
import com.example.classificationdragon.ui.HistoryActivity
import com.example.classificationdragon.ui.PreviewActivity
//import com.example.classificationdragon.models.BeritaTerbaru
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    val days = listOf("FRI", "SAT", "SUN", "MON", "TUE", "WED", "THU")
    val highs = listOf(4, 6, 8, 2, 3, 3, 5)
    val lows = listOf(1, 1, 1, 1, 1, 1, 2)


    private lateinit var binding: ActivityMainBinding
    private val apiKey = "2533e82274264e4889573b9f270b6bcf"

    private val STORAGE_REQUEST_CODE = 102
    private lateinit var selectButton: LinearLayout
    private lateinit var buttonCamera: LinearLayout
    private lateinit var buttonHistory: LinearLayout
    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageUri: Uri? = null

    private val klasifikasi by lazy { Klasifikasi(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvNews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

//        RetrofitClient.instance.getNews("Pertanian", "id", apiKey)
//            .enqueue(object : Callback<BeritaTerbaru> {
//                override fun onResponse(call: Call<BeritaTerbaru>, response: Response<BeritaTerbaru>) {
//                    if (response.isSuccessful) {
//                        val newsList = response.body()?.articles ?: emptyList()
//                        binding.rvNews.adapter = NewsAdapter(newsList)
//                        Log.d("API_SUCCESS", "Jumlah artikel: ${newsList.size}")
//
//                    } else {
//                        Log.e("Retrofit", "Response gagal: ${response.code()}")
//                        Toast.makeText(this@MainActivity, "Gagal mendapatkan berita. Code: ${response.code()}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<BeritaTerbaru>, t: Throwable) {
//                    Log.e("Retrofit", "Error: ${t.message}", t)
//                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
//                }
//            })



//        fetchArticles()
//        val apiKey = "b1014c055aa0a605aa8af1767d7361c1"
//        val kota = "Samarinda" // atau input dari user
//
//        RetrofitClient.instance.getWeatherByCity(kota, apiKey)
//            .enqueue(object : retrofit2.Callback<WeatherResponse> {
//                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
//                    if (response.isSuccessful) {
//                        val data = response.body()
//                        val suhu = data?.main?.temp
//                        val deskripsi = data?.weather?.firstOrNull()?.description
//                        val kelembapan = data?.main?.humidity
//
//                        // tampilkan data di UI
//                        binding.tvCuaca.text = "Cuaca di $kota: $deskripsi, $suhu°C, Humidity: $kelembapan%"
//                    }
//                }
//
//                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
//                    Toast.makeText(this@MainActivity, "Gagal memuat cuaca: ${t.message}", Toast.LENGTH_SHORT).show()
//                }
//            })


        loadClassificationHistory()

        selectButton = findViewById(R.id.selectButton)
        buttonHistory = findViewById(R.id.tombolRiwayat)
        buttonCamera = findViewById(R.id.buttonCamera)

        selectButton.setOnClickListener { requestPhotoVideoPermission() }

        buttonHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        buttonCamera.setOnClickListener { openCamera() }
    }
//    private fun fetchArticles() {
//        val url = "https://www.kompas.com"
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val doc = Jsoup.connect(url)
//                    .userAgent("Mozilla/5.0") // untuk menghindari pemblokiran bot
//                    .get()
//
//                val paragraphs = doc.select("p").map { it.text() }.filter { it.length > 30 }
//                val imageUrl = doc.select("figure img").firstOrNull()?.absUrl("src") ?: ""
//
//                val articleList = mutableListOf<Article>()
//
//                // Ambil title utama dari halaman
//                val title = doc.select("h1").firstOrNull()?.text() ?: "Artikel Buah Naga"
//
//                // Bagi isi paragraf jadi beberapa potong artikel
//                for (i in 0 until paragraphs.size step 2) {
//                    val desc = paragraphs.getOrNull(i) ?: continue
//                    articleList.add(Article(title, desc, imageUrl))
//                }
//
//                withContext(Dispatchers.Main) {
//                    binding.rvNews.adapter = NewsAdapter(articleList)
//                    Log.d("SCRAPING_SUCCESS", "Artikel ditemukan: ${articleList.size}")
//                }
//
//            } catch (e: IOException) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
//                    Log.e("SCRAPING_ERROR", "Gagal scraping: ${e.message}")
//                }
//            }
//        }
//    }


//    private fun fetchRssFeed() {
//        Thread {
//            try {
//                val url = "https://www.liputan6.com/tag/pertanian/rss"  // Ganti sesuai feed yang kamu pakai
//                val doc = Jsoup.connect(url).get()
//                val items = doc.select("item")
//
//                val newsList = mutableListOf<Article>()
//                for (item in items) {
//                    val title = item.select("title").text()
//                    val link = item.select("link").text()
//                    val description = item.select("description").text()
//
//                    val article = Article(title, link, description)
//                    newsList.add(article)
//                }
//
//                runOnUiThread {
//                    binding.rvNews.adapter = NewsAdapter(newsList)
//                    Log.d("RSS", "Jumlah artikel: ${newsList.size}")
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                runOnUiThread {
//                    Toast.makeText(this, "Gagal mengambil RSS: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }.start()
//    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Izin penyimpanan ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkPhotoVideoPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPhotoVideoPermission() {
        if (checkPhotoVideoPermission()) {
            openGallery()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
                )
            } else {
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
            .setPositiveButton("Izinkan") { _, _ -> requestPhotoVideoPermission() }
            .setNegativeButton("Tolak") { dialog, _ -> dialog.dismiss() }
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
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                permissions[Manifest.permission.READ_MEDIA_VIDEO] == true ||
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true

        if (granted) {
            openGallery()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) ||
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_VIDEO) ||
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                showPermissionRationale()
            } else {
                showSettingsDialog()
            }
        }
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
            klasifikasi.classifyImage(squareBitmap, savedImagePath, object :
                Klasifikasi.ClassificationCallback {
                override fun onResult(resultText: String, predictedDisease: String) {
                    val stream = ByteArrayOutputStream()
                    squareBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()

                    val intent = Intent(this@MainActivity, HasilDeteksi::class.java).apply {
                        putExtra("RESULT_TEXT", resultText)
                        putExtra("IMAGE", byteArray)
                        putExtra("DISEASE_NAME", predictedDisease)
                    }
                    startActivity(intent)
                }
            })
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            if (uri != null) {
                try {
                    val inputStream = contentResolver.openInputStream(uri)
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

    private fun loadClassificationHistory() {
        val db = HistoriDatabase.getDatabase(this)
        val dao = db.historyDao()

        CoroutineScope(Dispatchers.IO).launch {
            val historyList = dao.getAllHistory()
            historyList.forEach {
                Log.d("Database", "Riwayat: ${it.diseaseName} - ${it.date} - ${it.imagePath}")
            }
        }
    }
}
