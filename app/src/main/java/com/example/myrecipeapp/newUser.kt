package com.example.myrecipeapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.myrecipeapp.databinding.ActivityNewUserBinding

class newUser : AppCompatActivity() {
    private lateinit var binding: ActivityNewUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val registerButton = findViewById<ImageButton>(R.id.btnreg)
        registerButton.setOnClickListener{
            onRegisterButtonClick()
        }
    }

    fun onRegisterButtonClick(){
        //obtener los valores de los campos de entrada
        val email = binding.editmail.text.toString()
        val username = binding.edituser.text.toString()
        val password = binding.editpassword.text.toString()

        //instancia del DatabaseHelper

        val dbHelper = DatabaseHelper(this)

        //obtener una referencia a la base de datos en modo escritura

        val db = dbHelper.writableDatabase

        //crear un objeto ContentValues para almacenar los valores a introducir

        val values = ContentValues().apply {
            put("email", email)
            put("password", password )
            put("name", username)
        }

        //Insertar el nuevo usuario en la tabla 'users'
        val userId = db.insert("users", null, values)

        //cerrar la base de datos
        db.close()

        //redirigir a la siguiente actividad

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}