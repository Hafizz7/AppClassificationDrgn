package com.example.classificationdragon.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classificationdragon.R
import com.example.classificationdragon.data.db.HistoriDatabase
import com.example.classificationdragon.data.models.HistoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private var historyList = mutableListOf<HistoryEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadHistoryData()
    }

    private fun loadHistoryData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = HistoriDatabase.getDatabase(this@HistoryActivity)
                val dao = db.historyDao()
                val data = dao.getAllHistory() // Ambil semua data dari database

                runOnUiThread {
                    historyList.clear()
                    historyList.addAll(data)
                    historyAdapter = HistoryAdapter(this@HistoryActivity, historyList) { itemToDelete ->
                        deleteHistory(itemToDelete) // Hapus dari database
                    }
                    recyclerView.adapter = historyAdapter
                }
            } catch (e: Exception) {
                Log.e("Database", "Gagal mengambil data dari database", e)
            }
        }
    }
    private fun deleteHistory(history: HistoryEntity) {
        // Menghapus data dari database di dalam coroutine 34
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = HistoriDatabase.getDatabase(this@HistoryActivity)
                val dao = db.historyDao()
                dao.delete(history) // Hapus dari database

                // Panggil loadHistoryData() untuk memperbarui data setelah penghapusan
                loadHistoryData()
            } catch (e: Exception) {
                Log.e("Database", "Gagal menghapus data dari database", e)
            }
        }
    }



}
