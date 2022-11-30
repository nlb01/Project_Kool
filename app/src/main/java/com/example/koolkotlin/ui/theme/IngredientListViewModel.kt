package com.example.koolkotlin.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koolkotlin.APIinterface
import com.example.koolkotlin.Ingredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IngredientListViewModel(recipeId : Int) :ViewModel() {

    //    private val _recipes: MutableLiveData<List<RecipesItem>> = MutableLiveData()
    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    internal val ingredients : StateFlow<List<Ingredient>> get() = _ingredients


    init {
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://kool.blackab.repl.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(APIinterface::class.java)
            val call = service.getRecipeIngredients(recipeId)

            call.enqueue(object : Callback<List<Ingredient>> {

//                override fun onFailure(call: Call<List<RecipesItem>>, t: Throwable) {
//                    Log.d("rec", "error")
//                }

                override fun onResponse(call: Call<List<Ingredient>>, response: Response<List<Ingredient>>) {
                    if (response.isSuccessful) {
                        _ingredients.value = response.body()!!
                        Log.i("ingredient" , "Ingredient has been fetched!")
                        if (ingredients != null) {
                            Log.i("ingredient" , "ingredients not null!")
                        }
                    }
                }

                override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                    Log.i("ingredient", "error")
                }

            })
        }
    }
}