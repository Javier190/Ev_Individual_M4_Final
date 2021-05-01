package com.example.m4claseclase28

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

//Crea el metodo por el cual accedemos a nuestra API
interface APIService {
    @GET
    fun getCharacterByName(@Url url:String): Call<DogsResponse>
}