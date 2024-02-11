package com.example.myrecipeapp

class Recipe (
    val id: Long,
    val userId: Long,
    val name: String,
    val type: String,
    val imagePath: String,
    val ingredients: String,
    val description: String,
)