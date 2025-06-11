package com.example.classificationdragon.data.models

data class WeatherResponse(
    val name: String, // nama kota
    val weather: List<Weather>,
    val main: Main
)

data class Weather(
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val humidity: Int
)
