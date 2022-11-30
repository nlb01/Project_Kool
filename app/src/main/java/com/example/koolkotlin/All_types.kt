package com.example.koolkotlin

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object All_types {
    val all_types : ArrayList<Type> = ArrayList<Type>()
    var typeNames = ArrayList<String>()

    fun setNames() {
        typeNames = setTypeNames()
    }

    init {
        setTypes()
    }

    fun setTypeNames() : ArrayList<String> {
        val names = ArrayList<String>()
        Log.i("type" , "all types empty or null !" + all_types.isEmpty().toString())
        for (type in all_types) {
            Log.i("type" , type.Type)
            names.add(type.Type)
        }
        return names
    }


    fun setTypes(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kool.blackab.repl.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.i("type" , "Retrofit Initialized")
        val service = retrofit.create(APIinterface::class.java)
        val call = service.getTypes()
        call.enqueue(object : Callback<List<Type>> {
            override fun onResponse(call: Call<List<Type>>, response: Response<List<Type>>) {
                if (response.isSuccessful) {
                    Log.i("type" , "Response Successful")
                    val types = response.body()
                    Log.i("type" , "Types have been fetched!")
                    if (types != null) {
                        Log.i("type" , "Types not null and are being assigned globally!")
                        all_types.addAll(types)
                    }
                }
                else { Log.i("type" , "Not Failure but not Success!")}
            }

            override fun onFailure(call: Call<List<Type>>, t: Throwable) {
                Log.i("type", "error")
                Log.i("type", t.toString())
            }
        })
    }

}