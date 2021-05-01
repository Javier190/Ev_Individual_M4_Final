package com.example.m4claseclase28

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity: AppCompatActivity(), android.widget.SearchView.OnQueryTextListener {

    lateinit var rvDogs:RecyclerView
    lateinit var imagesPuppies:List<String>
    lateinit var dogsAdapter:DogsAdapter
    lateinit var searchBreed:SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBreed = findViewById(R.id.searchBreed)
        rvDogs = findViewById(R.id.rvDogs) as RecyclerView
        searchBreed.setOnQueryTextListener(this)
    }


    //Inicio del programa al agregar una raza en la barra de busqueda
    override fun onQueryTextSubmit(query: String?): Boolean {
        searchByName(query!!.toLowerCase())
        return true
    }

    //Para tareas que requieren conexion a internet, es requerido usar Hilos, para esto ANKO.
    //Se recibe la informacion en formato JSON y se le entrega al recycler si la tarea se realizo correctamente(status)
    private fun searchByName(query: String) {
        doAsync {
            val call = getRetrofit().create(APIService::class.java).getCharacterByName("$query/images").execute()
            val puppies = call.body() as DogsResponse
            uiThread {
                if(puppies.status == "success") {               //Obteniendo objeto DogsResponse se puede iniciar el recycler
                    setUpRecycler(puppies)
                } else{
                    showErrorDialog()
                }
            }
        }
    }

    //El objeto Dogsresponse tiene 2 atributos, solo nesecitamos las imagenes(lista de strings) para el Recycler
    private fun setUpRecycler(puppies:DogsResponse) {
        if(puppies.status == "success"){
            imagesPuppies = puppies.images
        }
        dogsAdapter = DogsAdapter(imagesPuppies)
        rvDogs.setHasFixedSize(true)
        rvDogs.layoutManager = LinearLayoutManager(this)
        rvDogs.adapter = dogsAdapter
    }

    //Metodo para usar la API-REST. Se entrega la parte URL que NO cambia
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://dog.ceo/api/breed/").addConverterFactory(GsonConverterFactory.create()).build()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // TODO("Not yet implemented")
        return true
    }

    private fun showErrorDialog() {
        alert("Error ! ! !") {
            yesButton { }
        }.show()
    }
}