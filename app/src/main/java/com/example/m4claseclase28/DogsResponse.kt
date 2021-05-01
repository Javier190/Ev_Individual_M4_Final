package com.example.m4claseclase28

import com.google.gson.annotations.SerializedName


//Este es el modelo de datos que se va a recuperar de la llamada a la API
data class DogsResponse (
    @SerializedName("status") var status:String
    ,@SerializedName("message") var images:List<String>
)




