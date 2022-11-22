package com.example.koolkotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.prefs.PreferencesFactory

class DBhelper (context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper (context, "Ing", factory, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val TABLE_NAME = "Ingredients"
        val ID_COL = "id"
        val NAME_COL = "Name"
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " int PRIMARY KEY AUTO_INCREMENT, " +
                NAME_COL + " TEXT" + ")")

        // we are calling sqlite
        // method for executing our query
        if (db != null) {
            db.execSQL(query)
            addIngredient(db , "Cumin")
            addIngredient(db , "Chicken broth")
            addIngredient(db , "Light soy")
            addIngredient(db , "Dark Soy Sauce")
            addIngredient(db , "Chinese wine")
            addIngredient(db , "Chinese black vinegar")
            addIngredient(db , "Sichuan pepper")
            addIngredient(db , "sugar")
            addIngredient(db , "cornstarch")
            addIngredient(db , "garlic")
            addIngredient(db , "ginger")
            addIngredient(db , "red bell pepper")
            addIngredient(db , "green bell pepper")
            addIngredient(db , "dried chilies")
            addIngredient(db , "sesame oil")
            addIngredient(db , "unsalted peanuts")
        }
    }


    fun addIngredient(db: SQLiteDatabase , name : String){
        val values = ContentValues()

        values.put("Name", name)

        db.insert("Ingredients", null, values)
        db.close()
    }

    fun getIngredients(name:String) : Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("Select * From Ingredients Where Name Like \"$name%\" ",  null)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}