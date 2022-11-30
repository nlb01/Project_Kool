package com.example.koolkotlin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIinterface {

    @GET("recipes")
    fun getRecipes(): Call<List<RecipesItem>>

    @GET("types")
    fun getTypes(): Call<List<Type>>

    @GET("ingredients?ingredient_id={id}")
    fun getIngredient(@Path("id") id: Int): Call<List<Ingredient>>

    @GET("recipes")
    fun getRecipe(@Query("recipe_id") id: Int): Call<List<RecipesItem>>

    @GET("ingredients")
    fun getIngredients(): Call<List<Ingredient>>

    @GET("ingredients/{recipe_id}")
    fun getRecipeIngredients(@Path("recipe_id") recipe_id: Int): Call<List<Ingredient>>
}