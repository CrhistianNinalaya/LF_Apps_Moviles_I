package com.ninalaya.lf_ninalaya_crhistian

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ninalaya.lf_ninalaya_crhistian.databinding.ShowUserBinding

class UserShow : AppCompatActivity() {
    private lateinit var binding: ShowUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("USER_ID", -1)
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userPassword = intent.getStringExtra("USER_PASSWORD")
        val userName = intent.getStringExtra("USER_NAME")
        val userRole = intent.getStringExtra("USER_ROLE")
        val userCreationAt = intent.getStringExtra("USER_CREATION_AT")
        val userUpdatedAt = intent.getStringExtra("USER_UPDATED_AT")

        binding.tvIdValue.text = userId.toString()
        binding.tvEmailValue.text = userEmail
        binding.tvPasswordValue.text = userPassword
        binding.tvNameValue.text = userName
        binding.tvRoleValue.text = userRole
        binding.tvCreationValue.text = userCreationAt
        binding.tvUpdateValue.text = userUpdatedAt

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
