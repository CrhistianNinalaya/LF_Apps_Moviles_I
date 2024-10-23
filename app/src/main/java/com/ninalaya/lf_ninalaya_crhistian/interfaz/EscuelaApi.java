package com.ninalaya.lf_ninalaya_crhistian.interfaz;

import com.ninalaya.lf_ninalaya_crhistian.model.Usuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EscuelaApi {
    @GET("users")
    Call<List<Usuarios>> getUsuarios();
}
