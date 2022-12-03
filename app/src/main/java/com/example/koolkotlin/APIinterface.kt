package com.example.koolkotlin

import retrofit2.Call
import retrofit2.http.*

interface APIinterface {

    //Get All Recipes
    @GET("recipes")
    fun getRecipes(): Call<List<RecipesItem>>

    //Get all Types
    @GET("types")
    fun getTypes(): Call<List<Type>>


    //Get Ingredient with Id {id}
    @GET("ingredients?ingredient_id={id}")
    fun getIngredient(@Path("id") id: Int): Call<List<Ingredient>>


    //Get Recipe with Id {id}
    @GET("recipes")
    fun getRecipe(@Query("recipe_id") id: Int): Call<List<RecipesItem>>


    //Get Comments of recipe with id {recipe_id}
    @GET("comments")
    fun getRecipeComments(@Query("recipe_id") recipe_id: Int): Call<List<Comment>>


    //Get all Ingredients
    @GET("ingredients")
    fun getIngredients(): Call<List<Ingredient>>


    //Get all Ingredients of Recipe with Id {id}
    @GET("ingredients/{recipe_id}")
    fun getRecipeIngredients(@Path("recipe_id") recipe_id: Int): Call<List<Ingredient>>


    //Get all Recipes of Type {type}
    @GET("recipes/{type}")
    fun getTypeRecipes(@Path("type") type: String): Call<List<RecipesItem>>

    //Get Recipes with Ingredients {id1,id2,id3,...}
    @GET("recipes/ingredients")
    fun getRecipesWithIngredients(@Query("ingredients") ingredients: List<Int>): Call<List<RecipesItem>>

    //Post Recipe
    @POST("ingredients")
    fun addIngredient(@Body recipe: Ingredient): Call<Ingredient>

    //Post Comment
    @POST("comments")
    fun addComment(@Body comment: Comment): Call<Comment>

    //Post Recipe
    @POST("recipes")
    fun addRecipe(@Body recipe: RecipeIngredients): Call<RecipesItem>
}