package com.example.classificationdragon.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.classificationdragon.data.models.Klasifikasi
import com.example.classificationdragon.data.models.Klasifikasi.ClassificationCallback

class ClassificationViewModel(context: Context) : BaseViewModel() {
    private val klasifikasi = Klasifikasi(context)
    // Di ClassificationViewModel.kt
    var bitmap: Bitmap? = null

    private val _classificationResult = MutableLiveData<String>()
    val classificationResult: LiveData<String> = _classificationResult

    private val _predictedDisease = MutableLiveData<String>()
    val predictedDisease: LiveData<String> = _predictedDisease

    private val _isProcessing = MutableLiveData<Boolean>()
    val isProcessing: LiveData<Boolean> = _isProcessing

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun classifyImage(bitmap: Bitmap, imagePath: String) {
        _isProcessing.value = true
        klasifikasi.classifyImage(bitmap, imagePath, object : ClassificationCallback {
            override fun onResult(resultText: String, predictedDisease: String) {
                _classificationResult.postValue(resultText)
                _predictedDisease.postValue(predictedDisease)
                _isProcessing.postValue(false)
            }
        })
    }

    override fun handleError(throwable: Throwable) {
        _error.postValue(throwable.message ?: "Unknown error occurred")
        _isProcessing.postValue(false)
    }
}