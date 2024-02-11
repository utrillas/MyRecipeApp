package com.example.myrecipeapp

import android.annotation.SuppressLint
import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.EditText

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        const val DATABASE_NAME = "recipe_app_db"
        const val DATABASE_VERSION = 7

        const val TABLE_RECIPES = "recipes"
        const val TABLE_USERS = "users"
    }

    //definir la estructura de la tabla de usuarios
    private val CREATE_TABLE_USERS = """
        CREATE TABLE IF NOT EXISTS users(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        email TEXT UNIQUE,
        password TEXT,
        name TEXT
        )
    """.trimIndent()

    private val CREATE_TABLE_RECIPES = """
        CREATE TABLE IF NOT EXISTS recipes(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        userId INTEGER,
        name TEXT,
        type TEXT CHECK (type IN('Desayunos', 'Entrantes','Ensaladas', 'Primer plato', 'Plato principal', 'Postres', 'Cena ligeras')),
        imagePath TEXT,
        ingredients TEXT,
        description TEXT,

        FOREIGN KEY(userId) REFERENCES users(id)
        )
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        //crear las tablas al crear la base de datos

        db?.execSQL(CREATE_TABLE_USERS)
        db?.execSQL(CREATE_TABLE_RECIPES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Manejar la actualización de la base de datos si es necesario
        // (En este ejemplo, simplemente se elimina y se vuelve a crear la base de datos)
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS recipes")
        onCreate(db)
    }

    /*Esta función inserta la nueva receta en la base de datos*/

    fun insertRecipe(name: String, type: String, imagePath: String,ingredients: String, description: String ): Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put("userId", 1)
        values.put("name", name)
        values.put("type", type)
        values.put("imagePath", imagePath)
        values.put("ingredients", ingredients)
        values.put("description", description)


        val rowId = db.insert(TABLE_RECIPES, null, values)
        db.close()

        if(rowId != -1L){
            Log.d("Database" , "Recipe inserted successfully with ID: $rowId")
        } else{
            Log.d("Database", "Failed to insert recipe")
        }

        val count = getTotalRecipeCount()
        Log.d("Database", "Total recetas: $count")

        return rowId != -1L

    }
    //contar las recetas que tengo
    private fun getTotalRecipeCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_RECIPES", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    /*Esta función nos devuelve la receta que hemos seleccionado*/
    @SuppressLint("Range")
    fun searchRecipesByName(name: String): List<Recipe>{
        val recipes = mutableListOf<Recipe>()
        val db = readableDatabase
        val columns = arrayOf("id","userId", "name","type", "imagePath", "ingredients", "description")
        val selection = "name  = ?"
        val selectionArgs = arrayOf(name)

        val cursor: Cursor = db.query(TABLE_RECIPES, columns, selection, selectionArgs, null, null, null)

        while(cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val userId = cursor.getLong(cursor.getColumnIndex("userId"))
            val recipeName = cursor.getString(cursor.getColumnIndex("name"))
            val type = cursor.getString(cursor.getColumnIndex("type"))
            val imagePath = cursor.getString(cursor.getColumnIndex("imagePath"))
            val ingredients = cursor.getString(cursor.getColumnIndex("ingredients"))
            val description = cursor.getString(cursor.getColumnIndex("description"))

            val recipe = Recipe(id, userId, recipeName,  type , imagePath, ingredients, description)
            recipes.add(recipe)
        }
        cursor.close()
        db.close()

        return  recipes
    }

    fun deleteRecipe(recipeId:  Long): Boolean{
        val db = writableDatabase
        val deletedRows =db.delete(TABLE_RECIPES, "id = ?", arrayOf(recipeId.toString()))
        db.close()

        return deletedRows > 0
    }
}