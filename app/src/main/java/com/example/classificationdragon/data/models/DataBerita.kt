package com.example.classificationdragon.data.models

data class BeritaTerbaru(val articles: List<Article>)

data class Article(
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?
)
//data class Article(
//    val title: String,
//    val description: String,
//    val imageUrl: String
//)

