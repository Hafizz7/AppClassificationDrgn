package com.example.classificationdragon

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.classificationdragon.models.Article
import com.example.classificationdragon.databinding.ItemNewsBinding



class NewsAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        with(holder.binding) {
            tvTitle.text = article.title
//            tvDescription.text = article.description ?: "Deskripsi tidak tersedia"
            Glide.with(root.context).load(article.urlToImage).into(imgNews)

            root.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                root.context.startActivity(browserIntent)
            }
        }
    }
}