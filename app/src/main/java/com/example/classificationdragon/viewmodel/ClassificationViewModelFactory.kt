package com.example.classificationdragon.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.classificationdragon.viewmodel.ClassificationViewModel

class ClassificationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClassificationViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
