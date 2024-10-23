package com.ninalaya.lf_ninalaya_crhistian

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ninalaya.lf_ninalaya_crhistian.adapters.UsuariosAdapter
import com.ninalaya.lf_ninalaya_crhistian.databinding.ActivityMainBinding
import com.ninalaya.lf_ninalaya_crhistian.interfaz.EscuelaApi
import com.ninalaya.lf_ninalaya_crhistian.model.Usuarios
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var usuariosAdapter: UsuariosAdapter
    private lateinit var binding: ActivityMainBinding

    private lateinit var lstUsuarios: MutableList<Usuarios>
    private val ruta = "https://api.escuelajs.co/api/v1/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lstUsuarios = mutableListOf()

        usuariosAdapter = UsuariosAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = usuariosAdapter

        obtenerUsuarios()
    }

    private fun obtenerUsuarios() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(EscuelaApi::class.java)
        val call: Call<List<Usuarios>> = apiService.getUsuarios()

        call.enqueue(object : Callback<List<Usuarios>> {
            override fun onResponse(call: Call<List<Usuarios>>, response: Response<List<Usuarios>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    if (userList != null) {
                        usuariosAdapter = UsuariosAdapter(userList)
                        binding.recyclerView.adapter = usuariosAdapter
                    }
                } else {
                    println("Código de respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Usuarios>>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }
}