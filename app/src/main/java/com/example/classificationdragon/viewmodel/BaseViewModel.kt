package com.example.classificationdragon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected fun launchCoroutine(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                handleError(throwable)
            }
        ) {
            block()
        }
    }

    abstract fun handleError(throwable: Throwable)
}