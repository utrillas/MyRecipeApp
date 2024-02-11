package com.example.myrecipeapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.*
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import com.example.myrecipeapp.databinding.ActivityInsertRecipesBinding


class InsertRecipes : AppCompatActivity() {

    private lateinit var binding: ActivityInsertRecipesBinding
    private lateinit var databaseHelper: DatabaseHelper
    private val PICK_IMAGE_REQUEST_CODE = 123
    private var imagenUri: Uri? = null
    private val imagePath: String? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar databaseHelper
        databaseHelper = DatabaseHelper(this)

        // Spinner
        val spinner: Spinner = findViewById(R.id.spinner)
        val opcionesArray = resources.getStringArray(R.array.tipos_array)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesArray)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador

        // Botón para salir
        val inToexit = findViewById<ImageButton>(R.id.btnout)
        inToexit.setOnClickListener {
            val intent = Intent(this, RecipesUser::class.java)
            startActivity(intent)
        }

        // Boton para seleccionar una fotografía
        var capturebtn = findViewById<ImageButton>(R.id.capturebtn)

        capturebtn.setOnClickListener {
           openGallery()
        }

        val inToRecButton = findViewById<ImageButton>(R.id.btnInto)
        inToRecButton.setOnClickListener {
            inToRec() // Pasamos la vista como parámetro
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            imagenUri = data.data

            val imagePath = imagenUri.toString()

            findViewById<ImageView>(R.id.inphoto).setImageURI(imagenUri)
        }
    }
    fun inToRec() {
        // Obtener los valores de los campos de entrada
        val name = binding.inname.text.toString()
        val type = binding.spinner.selectedItem.toString()
        val ingredients = binding.inIngredients.text.toString()
        val description = binding.inreceta.text.toString()

        if (!imagePath.isNullOrBlank()){
            // Insertar receta en la base de datos
            val success = databaseHelper.insertRecipe(name, type, imagePath, ingredients, description)

            if (success) {
                Toast.makeText(
                    this,
                    "Se ha introducido la receta correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                clearFields()
            } else {
                Toast.makeText(this, "Fallo al introducir la receta", Toast.LENGTH_SHORT).show()
            }
        }else{
            var imagePath = "./camara.png"
            val success2 = databaseHelper.insertRecipe(name, type, imagePath, ingredients, description)

            if(success2){
                Toast.makeText(
                    this,
                    "Se ha introducido receta sin foto",
                    Toast.LENGTH_SHORT
                ).show()
                clearFields()
            }
        }

    }
    private fun clearFields(){
        val nameEditText = findViewById<EditText>(R.id.inname)
        val tipySpinner = findViewById<Spinner>(R.id.spinner)
        val imagePath = findViewById<ImageView>(R.id.inphoto)
        val ingredientesText = findViewById<TextView>(R.id.inIngredients)
        val descripcionText = findViewById<TextView>(R.id.inreceta)

        nameEditText.setText("")
        val spinnerArray = resources.getStringArray(R.array.tipos_array)
        val breakfastIndex = spinnerArray.indexOf("Desayuno")
        tipySpinner.setSelection(breakfastIndex)
        ingredientesText.setText("")
        descripcionText.setText("")

    }
}

