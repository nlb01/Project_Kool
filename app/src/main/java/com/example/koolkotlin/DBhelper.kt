package com.example.koolkotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import java.util.prefs.PreferencesFactory

class DBhelper (context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper (context, "Local_DATA", factory, 1){

    //Create all the tables { Recipes Table , Ingredients Table , Comments Table ,
    // Recipe - Ingredients Table , Recipe - Comments Table }
    override fun onCreate(db: SQLiteDatabase?) {
        val query1 = ("CREATE TABLE " + Ingredients_Table + " ("
                + in_ingred_id + " int PRIMARY KEY AUTO_INCREMENT, " +
                in_ingred_name + " TEXT" + ")")

        val query2 = ("CREATE TABLE " + Comments_Table + " ("
                + com_comment_id + " int PRIMARY KEY AUTO_INCREMENT, " +
                com_comment_content + " TEXT" + ")")
//
        val query3 = ("CREATE TABLE " + Recipe_Table + " ("
                + rec_recipe_id + " int PRIMARY KEY AUTO_INCREMENT, " +
                rec_recipe_duration + " INT," +
                rec_recipe_img + " TEXT," +
                rec_recipe_notes + " TEXT," +
                rec_recipe_rating + " INT," +
                rec_recipe_steps + " TEXT," +
                rec_recipe_style + " TEXT," +
                rec_recipe_title + " TEXT," +
                rec_recipe_type + " TEXT," +
                rec_recipe_vid + " TEXT," +")")

        val query4 = ("CREATE TABLE " + Recipe_Comments_Table + " ("
                + rc_comment_id + " int , " +
                rc_recipe_id + " int" + "," +
                "Foreign Key " + rc_comment_id + "References " + Comments_Table + "(" + com_comment_id + ")" +
                "Foreign Key " + rc_recipe_id + "References " + Recipe_Table + "(" + rec_recipe_id + ")" +
                "Primary Key ("+ rc_comment_id +  ", "  + rc_recipe_id + "))")

        val query5 = ("CREATE TABLE " + Recipe_Ingredients_Table + " ("
                + ri_ingredient_id + " int , " +
                ri_recipe_id + " int" + "," +
                "Foreign Key " + ri_ingredient_id + "References " + Ingredients_Table + "(" + in_ingred_id + ")" +
                "Foreign Key " + ri_recipe_id + "References " + Recipe_Table + "(" + rec_recipe_id + ")" +
                "Primary Key ("+ ri_ingredient_id +  ", "  + ri_recipe_id + "))")

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

    fun addRecipe(db:SQLiteDatabase , rec : RecipesItem, ingredients: List<Ingredient>) {
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

        val rec_id = getRecipeId(db , rec.Title)

        addRecipeIngredients(db, rec_id , ingredients)

    }


    //Add Ingredients of a recipe to Ingredients Table if not there already & to The Recipe - Ingredients Table
    //Takes as parameters Id of the recipe in the Recipe Table, List of ingredients of the Recipe as List<Ingredient>
    fun addRecipeIngredients(db:SQLiteDatabase , rec_id : Int, ingredients: List<Ingredient>) {
        for (ingred in ingredients) {
            if(!ingredInTable(ingred.Name , db)) {
                addIngredient(db , ingred.Name)
            }
            val ingred_id = getIngredientId(ingred.Name , db)
            val values = ContentValues()
            values.put(ri_ingredient_id, ingred_id)
            values.put(ri_recipe_id , rec_id)
            db.insert(Recipe_Ingredients_Table, null, values)
        }

        db.close()
    }

    //Adds ingredient with name ingred_name to the Ingredients Table
    fun addIngredient(db: SQLiteDatabase , ingred_name : String){
        val values = ContentValues()
        values.put(in_ingred_name, ingred_name)
        db.insert(Ingredients_Table, null, values)
        db.close()
    }


    //Adds comment with content name to the Comments Table
    fun addComment(db: SQLiteDatabase , name : String){
        val values = ContentValues()
        values.put(com_comment_content, name)
        db.insert(Comments_Table, null, values)
        db.close()
    }

    //Checks if ingredient with in_name is already stored in Ingredients Table
    fun ingredInTable(in_name : String, db: SQLiteDatabase) : Boolean {
        val query = ("SELECT EXISTS(SELECT * from " + Ingredients_Table +  "WHERE " + in_ingred_name +
                "= '"+ in_name +"')")
        val cursor : Cursor = db.rawQuery(query, null)
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;}

        cursor.close();
        return false
    }


    //Get Id of the recipe with Recipe Title recipe_title
    fun getRecipeId(db: SQLiteDatabase , recipe_title : String) : Int {
        val query = "Select" + rec_recipe_id + "From Recipe_Table Where" + rec_recipe_title + " Like \"$recipe_title%\" "
        val cursor : Cursor = db.rawQuery(query,  null)
        cursor.moveToFirst()
        return cursor.getInt(0)
    }


    //Get Id of the ingredient with name : name
    fun getIngredientId(name:String , db : SQLiteDatabase) : Int {
        val query = "Select" + in_ingred_id + "From Ingredients Where " + in_ingred_name + " Like \"$name%\" "
        val cursor : Cursor = db.rawQuery(query,  null)
        cursor.moveToFirst()
        return cursor.getInt(0)
    }


//    fun addRecipeComments(db: SQLiteDatabase , rec_id : Int, comments: List<Comment>) {
//        for (comment in comments) {
//            val values = ContentValues()
//            values.put(rc_comment_id, comment.Comment_id)
//            values.put(rc_recipe_id, rec_id)
//            db.insert(Recipe_Comments_Table , null , values)
//        }
//
//        db.close()
//    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

}