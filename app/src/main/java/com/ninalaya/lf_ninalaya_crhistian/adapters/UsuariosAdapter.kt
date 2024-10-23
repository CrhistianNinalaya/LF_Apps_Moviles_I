package com.ninalaya.lf_ninalaya_crhistian.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ninalaya.lf_ninalaya_crhistian.R
import com.ninalaya.lf_ninalaya_crhistian.databinding.ItemUserBinding
import com.ninalaya.lf_ninalaya_crhistian.model.Usuarios

class UsuariosAdapter(private val usuarios: List<Usuarios>, private val listener: OnUserClickListener) : RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {
    interface OnUserClickListener {
        fun onUserClick(userId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = usuarios[position]
        holder.bind(user)

        holder.itemView.setOnClickListener{
            listener.onUserClick(user.id)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemUserBinding.bind(view)

        fun bind(user: Usuarios) {
            binding.tvId.text = user.id.toString()
            binding.tvName.text = user.name
        }
    }
}