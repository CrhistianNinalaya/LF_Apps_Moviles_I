package com.ninalaya.lf_ninalaya_crhistian

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.ninalaya.lf_ninalaya_crhistian.databinding.FindUserBinding
import com.ninalaya.lf_ninalaya_crhistian.interfaz.EscuelaApi
import com.ninalaya.lf_ninalaya_crhistian.model.Usuarios
import com.ninalaya.lf_ninalaya_crhistian.model.UsuariosFB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FindUser : AppCompatActivity(){
    private lateinit var binding: FindUserBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()


        binding.btnBuscar.setOnClickListener {
            val userIdInput = binding.inputId.text.toString()
            if (userIdInput.isEmpty()) {
                showAlert("ingresa un ID valido")
            } else {
                val userId = userIdInput.toIntOrNull()
                if (userId != null) {
                    getUserById(userId)
                } else {
                    showAlert("Debes escribir un numero.")
                }
            }
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun getUserById(userId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(EscuelaApi::class.java)
        val call: Call<Usuarios> = apiService.getUsuarioPorId(userId)

        call.enqueue(object : Callback<Usuarios> {
            override fun onResponse(call: Call<Usuarios>, response: Response<Usuarios>) {
                when (response.code()) {
                    200 -> {
                        val user = response.body()
                        if (user != null) {
                            displayUserDetails(user)
                            checkUserInFirebase(user.id) { exists ->
                                if (!exists) {
                                    registerUserInFirebase(user.id, user.name, user.email)
                                } else {
                                    showAlert("Este usuario ya se encuentra en Firebase.")
                                }
                            }
                        } else {
                            showAlert("Llene el campo Por favor")
                        }
                    }
                    400 -> {
                        showAlert("Usuario no encontrado. Por favor")
                        displayUserDetailsWithNull()
                    }
                    404 -> {
                        showAlert("Usuario no encontrado.")
                        displayUserDetailsWithNull()
                    }

                    else -> {
                        showAlert("Error: ${response.code()}")
                    }
                }
                binding.inputId.text?.clear()
            }

            override fun onFailure(call: Call<Usuarios>, t: Throwable) {
                Log.e("FindUserActivity", "Error: ${t.message}")
                showAlert("Error de conexión.")
            }
        })
    }

    private fun displayUserDetails(user: Usuarios) {
        val userDetails = "ID: \n${user.id}\n" +
                "Email: \n${user.email}\n" +
                "Nombre: \n${user.name}\n" +
                "Rol: \n${user.role}\n" +
                "Creado: \n${user.creationAt}\n" +
                "Actualizado: \n${user.updatedAt}"
        binding.tvUserDetails.text = userDetails
    }

    private fun displayUserDetailsWithNull() {
        val id = "N/A"
        val email = "Empty"
        val name = "Empty"
        val role = "Empty"
        val creationAt = "Empty"
        val updateAt = "Empty"

        val userDetails = """
        ID: $id
        
        Email: $email
        Nombre: $name
        Rol: $role
        Creado: $creationAt
        Actualizado: $updateAt
    """.trimIndent()

        binding.tvUserDetails.text = userDetails
    }


    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Alerta")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun registerUserInFirebase(id: Int?, name: String?, email: String?) {
        if (id != null && name != null && email != null) {
            val userFirebase = UsuariosFB(id, name, email)

            database.reference.child("collection").child(id.toString()).setValue(userFirebase)
                .addOnSuccessListener {
                    showAlert("Usuario registrado en Firebase.")
                }
                .addOnFailureListener { e ->
                    showAlert("Error al registrar: ${e.message}")
                }
        }
    }

    private fun checkUserInFirebase(userId: Int, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("collection").child(userId.toString())
        database.get().addOnSuccessListener { snapshot ->
            callback(snapshot.exists())
        }.addOnFailureListener { e ->
            showAlert("No se puede verificar al usuario: ${e.message}")
            callback(false)
        }
    }
}