package com.example.classificationdragon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DiseaseInfoViewModel : BaseViewModel() {
    private val _diseaseName = MutableLiveData<String>()
    val diseaseName: LiveData<String> = _diseaseName

    private val _symptoms = MutableLiveData<String>()
    val symptoms: LiveData<String> = _symptoms

    private val _cause = MutableLiveData<String>()
    val cause: LiveData<String> = _cause

    private val _treatment = MutableLiveData<String>()
    val treatment: LiveData<String> = _treatment

    fun setDiseaseInfo(disease: String) {
        launchCoroutine {
            val diseaseData = getDiseaseData()
            diseaseData[disease]?.let { (symptoms, cause, treatment) ->
                _diseaseName.postValue(disease)
                _symptoms.postValue(symptoms)
                _cause.postValue(cause)
                _treatment.postValue(treatment)
            } ?: run {
                _diseaseName.postValue("Penyakit tidak diketahui")
                _symptoms.postValue("-")
                _cause.postValue("-")
                _treatment.postValue("-")
            }
        }
    }

    private fun getDiseaseData(): Map<String, Triple<String, String, String>> {
        return mapOf(
            "Antraknosa" to Triple(
                "- Batang berubah kuning\n- Mengalami kekeringan\n- Batang mengering dan mati",
                "- Infeksi jamur Neoscytalidium dimidiatum",
                "- Pangkas batang yang terserang\n- Bersihkan kebun secara rutin\n- Perbaiki drainase"
            ),
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
    }

    override fun handleError(throwable: Throwable) {
        // Handle error if needed
    }
}