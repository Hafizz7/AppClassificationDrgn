package com.example.classificationdragon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.classificationdragon.data.db.HistoriDao
import com.example.classificationdragon.data.models.HistoryEntity

class HistoryViewModel(private val historyDao: HistoriDao) : BaseViewModel() {
    private val _historyList = MutableLiveData<List<HistoryEntity>>()
    val historyList: LiveData<List<HistoryEntity>> = _historyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadHistory() {
        launchCoroutine {
            _isLoading.postValue(true)
            try {
                val data = historyDao.getAllHistory()
                _historyList.postValue(data)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to load history: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun deleteHistory(history: HistoryEntity) {
        launchCoroutine {
            try {
                historyDao.delete(history)
                loadHistory() // Refresh the list after deletion
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to delete history: ${e.message}")
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _errorMessage.postValue(throwable.message ?: "Unknown error occurred")
    }
}