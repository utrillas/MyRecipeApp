package com.example.myrecipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var  emailEditText: EditText
    private lateinit var passworEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TextView = findViewById<TextView>(R.id.newUser)

        emailEditText = findViewById(R.id.mail)
        passworEditText = findViewById(R.id.password)

        val buttonEnter = findViewById<ImageButton>(R.id.buttonEnter)
        buttonEnter.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passworEditText.text.toString()

            if (isValidLogin(email, password)){
                val intent = Intent(this, RecipesUser::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "email or password no valid", Toast.LENGTH_SHORT).show()
            }
        }

        TextView.setOnClickListener{
            val intent = Intent(this,newUser::class.java)
            startActivity(intent)
        }
    }
    private fun isValidLogin(email: String, password: String): Boolean{
        Log.d("etiqueta", "entro en la funcion")
        val db = DatabaseHelper(this).readableDatabase
        val projection = arrayOf("email", "password")
        val selection = "email = ? AND password = ?"
        val selectionArg = arrayOf(email, password)

        val cursor= db.query("users", projection,selection,selectionArg, null, null, null)

        val isValid = cursor.count > 0
        cursor.close()
        db.close()

        return  isValid

    }
}