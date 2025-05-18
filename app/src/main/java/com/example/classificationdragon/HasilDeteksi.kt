package com.example.classificationdragon

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.classificationdragon.models.HistoryEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class HasilDeteksi : AppCompatActivity() {

    private lateinit var titleText: TextView
    private lateinit var symptomsText: TextView
    private lateinit var causeText: TextView
    private lateinit var treatmentText: TextView
    private lateinit var imageView: ImageView
    private lateinit var hasilText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hasil_predeksi)

        // Inisialisasi tampilan
        imageView = findViewById(R.id.hasilPredek)
        titleText = findViewById(R.id.titleText)
        symptomsText = findViewById(R.id.symptomsText)
        causeText = findViewById(R.id.causeText)
        hasilText = findViewById(R.id.hasilText)
        treatmentText = findViewById(R.id.treatmentText)

        // Ambil data dari Intent
        val disease = intent.getStringExtra("DISEASE_NAME") ?: "Tidak Diketahui"
        val imageByteArray = intent.getByteArrayExtra("IMAGE")

        // Data penyakit (harus dideklarasikan sebelum digunakan)
        val diseaseData = mapOf(
            "Antraknosa" to Triple(
                "- Batang berubah kuning\n- Mengalami kekeringan\n- Batang mengering dan mati",
                "- Infeksi jamur *Neoscytalidium dimidiatum*",
                "- Pangkas batang   yang terserang\n- Bersihkan kebun secara rutin\n- Perbaiki drainase"
            ),
//            "Bercak Merah" to Triple(
//                "- Muncul bercak merah pada batang\n- Batang mengering",
//                "- Infeksi jamur tertentu",
//                "- Pangkas bagian terinfeksi\n- Gunakan fungisida"
//            ),
            "Busuk Batang" to Triple(
                "- Batang lunak dan membusuk\n- Muncul bau tidak sedap",
                "- Disebabkan oleh bakteri atau jamur",
                "- Kurangi kelembaban\n- Gunakan fungisida dan bakterisida"
            ),
            "Kudis" to Triple(
                "- Muncul kerak atau luka kasar pada batang",
                "- Infeksi jamur atau bakteri",
                "- Pangkas bagian yang terinfeksi\n- Semprot fungisida"
            ),
            "Mosaik" to Triple(
                "- Batang memiliki pola belang\n- Pertumbuhan terganggu",
                "- Disebabkan oleh virus mosaik",
                "- Gunakan tanaman tahan virus\n- Hindari serangga vektor"
            )
        )

        // Set data berdasarkan penyakit
        diseaseData[disease]?.let { (symptoms, cause, treatment) ->
            titleText.text = disease
            symptomsText.text = "$symptoms"
            causeText.text = "$cause"
            treatmentText.text = "$treatment"
        } ?: run {
            titleText.text = "Penyakit tidak diketahui"
            symptomsText.text = "-"
            causeText.text = "-"
            treatmentText.text = "-"
        }

        // Tampilkan gambar jika ada
        imageByteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            imageView.setImageBitmap(bitmap)
        }


    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // Menutup activity saat ini
    }

}
