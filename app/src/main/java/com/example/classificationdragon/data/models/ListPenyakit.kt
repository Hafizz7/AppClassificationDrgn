package com.example.classificationdragon.data.models

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.classificationdragon.R

class ListPenyakit: AppCompatActivity() {
    private lateinit var titleText: TextView
    private lateinit var symptomsText: TextView
    private lateinit var causeText: TextView
    private lateinit var treatmentText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_penyakit)

        titleText = findViewById(R.id.titleText)
        symptomsText = findViewById(R.id.symptomsText)
        causeText = findViewById(R.id.causeText)
        treatmentText = findViewById(R.id.treatmentText)

        // Ambil data dari intent
        val disease = intent.getStringExtra("DISEASE_NAME") ?: "Tidak Diketahui"

        // Data penyakit (bisa pakai database jika banyak)
        val diseaseData = mapOf(
            "Antraknosa" to Triple(
                "- Batang berubah kuning\n- Mengalami kekeringan\n- Batang mengering dan mati",
                "- Infeksi jamur Neoscytalidium dimidiatum",
                "- Pangkas batang yang terserang\n- Bersihkan kebun secara rutin\n- Perbaiki drainase"
            ),
            "Bercak Merah" to Triple(
                "- Muncul bercak hitam pada batang\n- Batang menjadi busuk",
                "- Infeksi jamur Colletotrichum gloeosporioides",
                "- Pangkas bagian terinfeksi\n- Semprot fungisida\n- Perbaiki sirkulasi udara"
            ),
            "Busuk Batang" to Triple(
                "- Muncul bercak hitam pada batang\n- Batang menjadi busuk",
                "- Infeksi jamur Colletotrichum gloeosporioides",
                "- Pangkas bagian terinfeksi\n- Semprot fungisida\n- Perbaiki sirkulasi udara"
            ),
            "Kudis" to Triple(
                "- Muncul bercak hitam pada batang\n- Batang menjadi busuk",
                "- Infeksi jamur Colletotrichum gloeosporioides",
                "- Pangkas bagian terinfeksi\n- Semprot fungisida\n- Perbaiki sirkulasi udara"
            ),
            "Mosaik" to Triple(
                "- Muncul bercak hitam pada batang\n- Batang menjadi busuk",
                "- Infeksi jamur Colletotrichum gloeosporioides",
                "- Pangkas bagian terinfeksi\n- Semprot fungisida\n- Perbaiki sirkulasi udara"
            )
        )

        // Set data berdasarkan penyakit
        diseaseData[disease]?.let { (symptoms, cause, treatment) ->
            titleText.text = disease
            symptomsText.text = symptoms
            causeText.text = cause
            treatmentText.text = treatment
        }
    }
}