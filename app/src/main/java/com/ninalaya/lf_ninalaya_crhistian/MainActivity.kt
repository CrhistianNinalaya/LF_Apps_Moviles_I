package com.ninalaya.lf_ninalaya_crhistian

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class MainActivity : AppCompatActivity(), UsuariosAdapter.OnUserClickListener {
    private lateinit var usuariosAdapter: UsuariosAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var lstUsuarios: MutableList<Usuarios>
    private val ruta = "https://api.escuelajs.co/api/v1/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lstUsuarios = mutableListOf()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        usuariosAdapter = UsuariosAdapter(lstUsuarios,this)
        binding.recyclerView.adapter = usuariosAdapter
        obtenerUsuarios()
        binding.btnConsultar.setOnClickListener {
            val intent = Intent(this, FindUser::class.java)
            startActivity(intent)
        }
    }
    private fun obtenerUsuarios() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(EscuelaApi::class.java)
        val call: Call<List<Usuarios>> = apiService.getUsuarios()
        call.enqueue(object : Callback<List<Usuarios>> {
            override fun onResponse(call: Call<List<Usuarios>>, response: Response<List<Usuarios>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    if (userList != null) {
                        usuariosAdapter = UsuariosAdapter(userList, this@MainActivity)
                        binding.recyclerView.adapter = usuariosAdapter
                    }
                } else {
                    println("Código de respuesta: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Usuarios>>, t: Throwable) {
                Log.e("MainActivity","Error: ${t.message}")
            }
        })
    }
    override fun onUserClick(userId: Int) {
        getUserById(userId)
    }
    private fun getUserById(userId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(EscuelaApi::class.java)
        val call: Call<Usuarios> = apiService.getUsuarioPorId(userId)

        call.enqueue(object : Callback<Usuarios> {
            override fun onResponse(call: Call<Usuarios>, response: Response<Usuarios>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Log.d("MainActivity", "Usuario obtenido: ID=${user.id}, Nombre=${user.name}")

                        showUserDetails(user)
                    }
                } else {
                    Log.e("MainActivity", "Error al obtener usuario: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Usuarios>, t: Throwable) {
                Log.e("MainActivity", "Error de conexión: ${t.message}")
            }
        })
    }
    private fun showUserDetails(user: Usuarios) {
        val intent = Intent(this, UserShow::class.java).apply {
            putExtra("USER_ID", user.id)
            putExtra("USER_EMAIL", user.email)
            putExtra("USER_PASSWORD", user.password)
            putExtra("USER_NAME", user.name)
            putExtra("USER_ROLE", user.role)
            putExtra("USER_CREATION_AT", user.creationAt)
            putExtra("USER_UPDATED_AT", user.updatedAt)
        }
        startActivity(intent)
    }
}