package com.example.koolkotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf
import java.util.prefs.PreferencesFactory

class DBhelper (context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper (context, "Local_DATA", factory, 1){

    //Create all the tables { Recipes Table , Ingredients Table , Comments Table ,
    // Recipe - Ingredients Table , Recipe - Comments Table }
    override fun onCreate(db: SQLiteDatabase?) {
        val query1 = "CREATE TABLE " + Ingredients_Table + " ( " + in_ingred_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                in_ingred_name + " TEXT );"

        val query2 = ("CREATE TABLE " + Comments_Table + " ("
                + com_comment_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                com_comment_content + " TEXT" + ");")
//
        val query3 = ("CREATE TABLE " + Recipe_Table + " ("
                + rec_recipe_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                rec_recipe_duration + " INTEGER," +
                rec_recipe_img + " TEXT," +
                rec_recipe_notes + " TEXT," +
                rec_recipe_rating + " INTEGER," +
                rec_recipe_steps + " TEXT," +
                rec_recipe_style + " TEXT," +
                rec_recipe_title + " TEXT," +
                rec_recipe_type + " TEXT," +
                rec_recipe_vid + " TEXT" +");")

        val query4 = ("CREATE TABLE " + Recipe_Comments_Table + " ("
                + rc_comment_id + " INTEGER, " +
                rc_recipe_id + " INTEGER" + "," +
                "FOREIGN KEY (" + rc_comment_id + ") References " + Comments_Table + "(" + com_comment_id + "), " +
                "Foreign Key (" + rc_recipe_id + ") References " + Recipe_Table + "(" + rec_recipe_id + "), " +
                "Primary Key ("+ rc_comment_id +  ", "  + rc_recipe_id + "));")

        val query5 = ("CREATE TABLE " + Recipe_Ingredients_Table + " ("
                + ri_ingredient_id + " INTEGER , " +
                ri_recipe_id + " INTEGER" + "," +
                "Foreign Key (" + ri_ingredient_id + ") References " + Ingredients_Table + "(" + in_ingred_id + "), " +
                "Foreign Key (" + ri_recipe_id + ") References " + Recipe_Table + "(" + rec_recipe_id + "), " +
                "Primary Key ("+ ri_ingredient_id +  ", "  + ri_recipe_id + "));")

        // we are calling sqlite
        // method for executing our query
        if (db != null) {
            //Create Ingredients Table
            db.execSQL(query1)

            //Create Comments Table
            db.execSQL(query2)

            //Create Recipes Table
            db.execSQL(query3)

            //Create Recipe - Comments Table
            db.execSQL(query4)

            //Create Recipe - Ingredients Table
            db.execSQL(query5)
        }
    }


    //Companion Objects holding all the constants
    companion object {
        private val DB_NAME = "Local_DATA"
        private val DB_VERSION = 1

        //Ingredient Table Columns
        val Ingredients_Table = "Ingredients"
        val in_ingred_id = "ingred_id"
        val in_ingred_name = "ingred_name"

        // Recipe Table Columns
        val Recipe_Table = "Recipe"
        val rec_recipe_id = "rec_id"
        val rec_recipe_title = "rec_title"
        val rec_recipe_duration = "rec_duration"
        val rec_recipe_style = "rec_style"
        val rec_recipe_type = "rec_type"
        val rec_recipe_steps = "rec_steps"
        val rec_recipe_notes = "rec_notes"
        val rec_recipe_vid = "rec_vid"
        val rec_recipe_img = "rec_img"
        val rec_recipe_rating = "rec_rating"


        //Comments Table Columns
        val Comments_Table = "Comments"
        val com_comment_id = "comment_id"
        val com_comment_content = "comment_name"

        //Recipe Comment Table
        val Recipe_Comments_Table = "Recipe_Comments"
        val rc_recipe_id = "recipe_id"
        val rc_comment_id = "comment_id"

        //Recipe Ingredient Table
        val Recipe_Ingredients_Table = "Recipe_Ingredients"
        val ri_recipe_id = "recipe_id"
        val ri_ingredient_id = "igredient_id"


    }


    //Add a recipe to the database -- Takes in a recipe Item and the recipe's ingredints list as List<Ingredient>
    fun addRecipe(rec : RecipesItem, ingredients: List<Ingredient> , comments: List<Comment>) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(rec_recipe_duration, rec.Duration)
        values.put(rec_recipe_notes, rec.Notes)
        values.put(rec_recipe_img, rec.IMG.toString())
        values.put(rec_recipe_rating, rec.Rating)
        values.put(rec_recipe_steps, rec.Steps)
        values.put(rec_recipe_style, rec.Style)
        values.put(rec_recipe_title, rec.Title)
        values.put(rec_recipe_type, rec.Type)
        values.put(rec_recipe_vid, rec.VID_URL)
        db.insert(Recipe_Table , null, values)

        val rec_id = getRecipeId(rec.Title)

        addRecipeIngredients(rec_id , ingredients)
        addRecipeComments(rec_id ,  comments)

        db.close()
    }


    //Add Ingredients of a recipe to Ingredients Table if not there already & to The Recipe - Ingredients Table
    //Takes as parameters Id of the recipe in the Recipe Table, List of ingredients of the Recipe as List<Ingredient>
    fun addRecipeIngredients(rec_id : Int, ingredients: List<Ingredient>) {
        val db = this.writableDatabase
        for (ingred in ingredients) {
            if(!ingredInTable(ingred.Name)) {
                addIngredient(ingred.Name)
            }
            val ingred_id = getIngredientId(ingred.Name)
            val values = ContentValues()
            values.put(ri_ingredient_id, ingred_id)
            values.put(ri_recipe_id , rec_id)
            db.insert(Recipe_Ingredients_Table, null, values)
        }

    }

    //Adds ingredient with name ingred_name to the Ingredients Table
    fun addIngredient(ingred_name : String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(in_ingred_name, ingred_name)
        db.insert(Ingredients_Table, null, values)

    }


    //Add Ingredients of a recipe to Ingredients Table if not there already & to The Recipe - Ingredients Table
    //Takes as parameters Id of the recipe in the Recipe Table, List of ingredients of the Recipe as List<Ingredient>
    fun addRecipeComments(rec_id : Int, comments: List<Comment>) {
        val db = this.writableDatabase
        for (comment in comments) {
            if(!commentInTable(comment.comment)){
                addComment(comment.comment)
            }
            val comment_id = getCommentId(comment.comment)
            val values = ContentValues()
            values.put(rc_recipe_id, rec_id)
            values.put(rc_comment_id , comment_id)
            db.insert(Recipe_Comments_Table, null, values)
        }

    }

    //Adds comment with content name to the Comments Table
    fun addComment(name : String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(com_comment_content, name)
        db.insert(Comments_Table, null, values)
    }


    //ALL GETTERS//

    //Returns all Recipes in the database
    //Output -> List of Recipes (A recipe is of type <List<String>>
    @SuppressLint("Range")
    fun getAllRecipes(): ArrayList<List<String>> {
        val db = this.readableDatabase
        val out = ArrayList<List<String>>()
        val query = "Select $rec_recipe_id From " + Recipe_Table
        val cursor : Cursor = db.rawQuery(query , null)

        while (cursor.moveToNext()){
            val recipe = getRecipe(cursor.getInt(0), false)
            out.add(recipe)
        }
        Log.i("db" , "Function getAllBookmareked Recipes successfully returned")
        return out
    }


    //Returns A recipe based on Id
    //Parameters:
    //-> ID: int is the recipe's id in Recipe Table
    //-> detail: Boolean that returns all attributes of the recipes when true
    //              and returns only (title, img, type, style, duration, three ingredients) when false
    @SuppressLint("Range")
    fun getRecipe(id: Int, detail : Boolean ) : List<String> {
        val db = this.readableDatabase
        val query = "Select * From $Recipe_Table Where $rec_recipe_id = $id"
        val cursor : Cursor = db.rawQuery(query , null)
        cursor.moveToFirst()
        var recipe = ArrayList<String>()
        val title = cursor.getString(cursor.getColumnIndex("rec_title"))
        val duration = cursor.getInt(cursor.getColumnIndex("rec_duration"))
        val type = cursor.getString(cursor.getColumnIndex("rec_type"))
        val style = cursor.getString(cursor.getColumnIndex("rec_style"))
        val img = cursor.getString(cursor.getColumnIndex("rec_img"))

        val recipe_ingredients = getRecipeIngredients(id, detail)
        val ingredients = ingredientsToString(recipe_ingredients)

        recipe.add(title)
        recipe.add(duration.toString())
        recipe.add(type)
        recipe.add(style)
        recipe.add(img)
        recipe.add(ingredients)
        recipe.add(id.toString())

        if(!detail) {
            return recipe
        }
        val steps = cursor.getString(cursor.getColumnIndex("rec_steps"))
        val notes = cursor.getString(cursor.getColumnIndex("rec_notes"))
        val vid = cursor.getString(cursor.getColumnIndex("rec_vid"))
        val rating = cursor.getInt(cursor.getColumnIndex("rec_rating"))

        recipe.add(steps)
        recipe.add(notes)
        recipe.add(vid)
        recipe.add(rating.toString())

        return recipe
    }

    //Get Id of the recipe with Recipe Title recipe_title
    fun getRecipeId(recipe_title : String) : Int {
        val db = this.readableDatabase
        val query = "Select " + rec_recipe_id + " From "+  Recipe_Table + " Where " + rec_recipe_title + " Like '$recipe_title' "
        val cursor : Cursor = db.rawQuery(query,  null)
        cursor.moveToFirst()
        val out = cursor.getInt(0)
        cursor.close()
        return out
    }

    @SuppressLint("Range")
    fun getAllIngredients() : ArrayList<String>{
        val db = this.readableDatabase
        val lis = ArrayList<String>()
        val query = "Select * From $Ingredients_Table"

        val cursor : Cursor = db.rawQuery(query , null)
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex("ingred_name"))
            lis.add(name)
        }
        return lis
    }

    //Get ingredients of a specific recipe
    //Parameters:
    //-> rec_id : int is recipe's id in the Recipes Table
    //-> detail: Boolean which returns only three ingredients when false and all ingredients when set to true
    @SuppressLint("Range")
    fun getRecipeIngredients(rec_id: Int, detail : Boolean) : List<String> {
        val db = this.readableDatabase
        val query = "Select $ri_ingredient_id From $Recipe_Ingredients_Table Where $ri_recipe_id = $rec_id"
        val ingred_list = ArrayList<String>()
        val cursor : Cursor = db.rawQuery(query, null)
        var count = 0
        while(cursor.moveToNext()) {
            if(detail == false && count == 3) {
                break
            }
            val id = cursor.getInt(cursor.getColumnIndex("igredient_id"))
            ingred_list.add(getIngredientName(id))
        }
        return ingred_list
    }

    //Get Id of the ingredient with name : name
    fun getIngredientId(name:String) : Int {
        val db = this.readableDatabase
        val query = "Select " + in_ingred_id + " From Ingredients Where " + in_ingred_name + " Like \'$name\' "
        val cursor : Cursor = db.rawQuery(query,  null)
        cursor.moveToFirst()
        val out = cursor.getInt(0)
        cursor.close()
        return out
    }

    //Get Name of the ingredient with id : id
    fun getIngredientName(id: Int ) : String {
        val db = this.readableDatabase
        val query = "Select $in_ingred_name From $Ingredients_Table Where $in_ingred_id = $id"
        val cursor : Cursor = db.rawQuery(query , null)
        cursor.moveToFirst()
        val out = cursor.getString(0)
        return out
    }

    //Checks if ingredient with in_name is already stored in Ingredients Table
    fun ingredInTable(in_name : String) : Boolean {
        val db = this.readableDatabase
        val query = ("SELECT EXISTS(SELECT * from " + Ingredients_Table +  " WHERE " + in_ingred_name +
                " = '"+ in_name +"')")
        val cursor : Cursor = db.rawQuery(query, null)
        cursor.moveToFirst();

        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;}

        cursor.close();
        return false
    }

    //Convert List of ingredient names to one string to display in the ingredients text view
    fun ingredientsToString(ingreds : List<String>) : String{
        var ingredients = ""
        for (i in 0..ingreds.size - 1) {
            if( i == ingreds.size - 1) {
                ingredients += ingreds[i] + "."
            }
            else {
                ingredients += ingreds[i]+ ", "
            }
        }
        return ingredients
    }


    //get Id of the comment with content name
    fun getCommentId(name: String) : Int {
        val db = this.readableDatabase
        val query = "Select " + com_comment_id + " From Comments Where " + com_comment_content + " Like '$name'"
        val cursor : Cursor = db.rawQuery(query,  null)
        cursor.moveToFirst()
        val out = cursor.getInt(0)
        cursor.close()
        return out
    }


    //Checks if comment with in_name is already stored in Comments Table
    fun commentInTable(in_name : String) : Boolean {
        val db = this.readableDatabase
        val query = ("SELECT EXISTS(SELECT * FROM " + Comments_Table +  " WHERE " + com_comment_content +
                " = '"+ in_name +"')")
        val cursor : Cursor = db.rawQuery(query, null)
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            db.close()
            return true;}

        cursor.close();
        return false
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

}