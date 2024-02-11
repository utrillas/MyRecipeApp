package com.example.myrecipeapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.myrecipeapp.DatabaseHelper
import com.bumptech.glide.Glide

class SearchRecipes : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var selectedRecipe: Recipe
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var  allRecipes: List<Recipe>

    @SuppressLint("WrongViewCast")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipes)

        databaseHelper = DatabaseHelper(this)

        val types = arrayOf("breakfast", "starters", "first course", "salads", "main dish", "desserts", "light dinners")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)

        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())

        val searchButton = findViewById<ImageButton>(R.id.search_button)
        searchButton.setOnClickListener {
            val searchName = findViewById<EditText>(R.id.sname).text.toString()
            searchRecipes(searchName)
        }



        val deleteButton = findViewById<ImageButton>(R.id.btndelete)
        deleteButton.setOnClickListener {
            if(::selectedRecipe.isInitialized){
                val deleted = databaseHelper.deleteRecipe(selectedRecipe.id)
                if (deleted){
                    Toast.makeText(this, "Receta eliminada correctamente", Toast.LENGTH_SHORT).show()
                    searchRecipes("")
                }else{
                    Toast.makeText(this, "Error al eliminar la receta", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "No hay receta seleccionada", Toast.LENGTH_SHORT).show()
            }
        }
    }

            private fun searchRecipes(name: String){
        allRecipes = databaseHelper.searchRecipesByName(name)

        if (allRecipes.isNotEmpty()){
            selectedRecipe = allRecipes[0]
            showRecipeDetails()
        }
        val recipesNames = allRecipes.map{ it.name }
        //se usa listAdapter para actualizar los datos en la vista
        listAdapter.clear()
        listAdapter.addAll(recipesNames)
    }

    //función para mostrar la receta selecionada
    private fun showRecipeDetails(){
        if (::selectedRecipe.isInitialized){
            val nameTextView = findViewById<TextView>(R.id.outname)
            val tyTextView = findViewById<TextView>(R.id.tipo)
            val photoImageView = findViewById<ImageView>(R.id.outphoto)
            val ingredientesTextView = findViewById<TextView>(R.id.outIngredients)
            val recetaTextView = findViewById<TextView>(R.id.outreceta)

            nameTextView.text = selectedRecipe.name
            tyTextView.text = selectedRecipe.type
            //cargamos la imagen con Glide
            Glide.with(this)
                .load(selectedRecipe.imagePath) //ruta de la imagen
                .placeholder(R.drawable.placeholder_image) //imagen de carga mientras se carga la imagen real
                .error(R.drawable.comida4) //Imagen error si la carga ha fallado
                .into(photoImageView)
            ingredientesTextView.text = selectedRecipe.ingredients
            recetaTextView.text = selectedRecipe.description
        } else{
        Toast.makeText(this,"Receta no encontrada", Toast.LENGTH_SHORT).show()
        }

    }
}