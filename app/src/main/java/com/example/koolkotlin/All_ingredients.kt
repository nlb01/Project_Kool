package com.example.koolkotlin

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object All_ingredients {
    val all_ingredients : ArrayList<Ingredient> = ArrayList<Ingredient>()
    var ingredientNames = ArrayList<String>()

    fun setNames() {
        ingredientNames = setIngredientNames()
    }

    init {
        setIngredients()
    }

    fun setIngredientNames() : ArrayList<String> {
        val names = ArrayList<String>()
        Log.i("ingred" , "all ingredients empty or null !" + all_ingredients.isEmpty().toString())
        for (ingredient in all_ingredients) {
            names.add(ingredient.Name)
        }
        return names
    }


    fun setIngredients(){
        val retrofit = Retrofit.Builder()
        .baseUrl("https://kool.blackab.repl.co/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        Log.i("ingred" , "Retrofit Initialized")
        val service = retrofit.create(APIinterface::class.java)
        val call = service.getIngredients()
        call.enqueue(object : Callback<List<Ingredient>> {
            override fun onResponse(call: Call<List<Ingredient>>, response: Response<List<Ingredient>>) {
                if (response.isSuccessful) {
                    Log.i("ingred" , "Response Successful")
                    val ingredients = response.body()
                    Log.i("ingred" , "Ingredients have been fetched!")
                    if (ingredients != null) {
                        Log.i("ingred" , "Ingredients not null and are being assigned globally!")
                        all_ingredients.addAll(ingredients)
                    }
                }
                else { Log.i("ingred" , "Not Failure but not Success!")}
            }

            override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                Log.i("ingred", "error")
                Log.i("ingred", t.toString())
            }
        })
    }

}