package com.ivan.animals.model

import com.google.gson.annotations.SerializedName

data class Animal(
    val name: String,
    val taxonomy: Taxonomy,
    val location: String,
    val speed: Speed,
    val diet: String,
    @SerializedName("lifespan")
    val lifeSpan: String,
    @SerializedName("image")
    val imageUrl: String
)