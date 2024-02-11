package com.example.myrecipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import androidx.viewpager.widget.ViewPager


class RecipesUser : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ImagePagerAdapter
    private val imageList = listOf(
        R.drawable.cena_ligera,
        R.drawable.desayunos,
        R.drawable.ensalada,
        R.drawable.entrantes,
        R.drawable.plato_principal,
        R.drawable.postres
    )
    private var currentPage = 0
    private val handler = Handler()
    private val delay: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_user)

        //boton de salida
        var btnscape = findViewById<ImageButton>(R.id.btnscape)

        btnscape.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        viewPager = findViewById(R.id.viewPager)

        pagerAdapter = ImagePagerAdapter(this, imageList)
        viewPager.adapter = pagerAdapter

        //boton para ir a añadir

        val btnAdd = findViewById<ImageButton>(R.id.btnadd)
        btnAdd.setOnClickListener{
            val intent = Intent(this, InsertRecipes::class.java)
            startActivity(intent)
        }

        //botón para ir a la pagina de buscar/eliminar

        val btnsearch = findViewById<ImageButton>(R.id.btnsearch)
        btnsearch.setOnClickListener {
            val intent = Intent(this, SearchRecipes::class.java)
            startActivity(intent)
        }

        //cambiar automaticamente las imagenes cada cierto intervalo de tiempo

        handler.postDelayed(object : Runnable{
            override fun run() {
                if (currentPage == imageList.size){
                    currentPage = 0
                }
                viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, delay)
            }
        }, delay)
    }


}