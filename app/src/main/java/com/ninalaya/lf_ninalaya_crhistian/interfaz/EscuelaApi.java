package com.ninalaya.lf_ninalaya_crhistian.interfaz;

import com.ninalaya.lf_ninalaya_crhistian.model.Usuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EscuelaApi {
    @GET("users")
    Call<List<Usuarios>> getUsuarios();

    @GET("users/{id}")
    Call<Usuarios> getUsuarioPorId(@Path("id") int id);
}
