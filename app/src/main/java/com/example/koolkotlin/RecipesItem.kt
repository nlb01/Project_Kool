package com.example.koolkotlin

data class RecipesItem(
    val Duration: Int,
    val IMG: IMG?,
    val Notes: String,
    val Rating: Int,
    val Steps: String,
    val Style: String,
    val Title: String,
    val Type: String,
    val VID_URL: String,
    var recipe_id: Int

)