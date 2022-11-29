package com.example.koolkotlin

import retrofit2.Call
import retrofit2.http.GET

interface APIinterface {

    @GET("recipes")
    fun getRecipes(): Call<List<RecipesItem>>

}