package com.example.classificationdragon.adapters

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.classificationdragon.R
import com.example.classificationdragon.models.HistoryEntity
import com.bumptech.glide.Glide

class HistoryAdapter(
    private val context: Context,
    private val historyList: MutableList<HistoryEntity>,
    private val deleteItem: (HistoryEntity) -> Unit // Callback untuk delete
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageHistory: ImageView = view.findViewById(R.id.imageHistory)
        val tvDiseaseName: TextView = view.findViewById(R.id.tvDiseaseName)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.riwayat_klasifikasi, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]

        holder.tvDiseaseName.text = history.diseaseName

        // Pisahkan tanggal dan waktu dari format yang disimpan di database
        val dateTimeParts = history.date.split(" ")
        if (dateTimeParts.size == 2) {
            holder.tvDate.text = "Tgl: ${dateTimeParts[0]}"
            holder.tvTime.text = "Jam: ${dateTimeParts[1]}"
        }
        // Set delete button action with confirmation dialog
        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                .setPositiveButton("Ya") { _, _ ->
                    // Panggil callback untuk menghapus data dari database
                    deleteItem(history)

                    // Hapus item dari historyList dan update UI
                    historyList.removeAt(position)
                    notifyItemRemoved(position)
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss() // Menutup dialog jika pengguna membatalkan
                }
            builder.create().show()
        }

        // Gunakan Glide untuk memuat gambar secara efisien
        Glide.with(context)
            .load(history.imagePath)
            .placeholder(R.drawable.placeholder_image) // opsional: gambar saat loading
//            .error(R.drawable.error_image)             // opsional: gambar jika gagal
            .into(holder.imageHistory)
    }


    override fun getItemCount(): Int = historyList.size
}
