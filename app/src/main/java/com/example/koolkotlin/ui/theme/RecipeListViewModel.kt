package com.example.koolkotlin.ui.theme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koolkotlin.APIinterface
import com.example.koolkotlin.RecipesItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeListViewModel(type :String) :ViewModel() {

//    private val _recipes: MutableLiveData<List<RecipesItem>> = MutableLiveData()
    private val _recipes = MutableStateFlow<List<RecipesItem>>(emptyList())
    internal val recipes : StateFlow<List<RecipesItem>> get() =  _recipes


    init {
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://kool.blackab.repl.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(APIinterface::class.java)

            var call : Call<List<RecipesItem>>
            if(type == "none")
                call = service.getRecipes()
            else
                call = service.getTypeRecipes(type)

            call.enqueue(object : Callback<List<RecipesItem>> {
                override fun onResponse(call: Call<List<RecipesItem>>, response: Response<List<RecipesItem>>) {
                    if (response.isSuccessful) {
                        _recipes.value = response.body()!!
                        Log.i("rec" , "recipes have been fetched!")
                        if (recipes != null) {
                            Log.i("rec" , "recipes not null!")
                        }
                    }
                }

                override fun onFailure(call: Call<List<RecipesItem>>, t: Throwable) {
                    Log.d("rec", "error")
                }

            })
        }
    }
}