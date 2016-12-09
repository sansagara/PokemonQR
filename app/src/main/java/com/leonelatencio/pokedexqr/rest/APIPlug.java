package com.leonelatencio.pokedexqr.rest;

import com.leonelatencio.pokedexqr.models.Pokemon;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Endpoint Definitions
 */

public interface APIPlug {

    /*
    These methods defines our API endpoints.
    All REST methods such as GET,POST,UPDATE,DELETE can be stated in here.
    */
	@GET("pokemon")
    Call<List<Pokemon>> getPokemon(@Query("p_start") int start, @Query("type") String type);

    @GET("pokemon/name")
    Call<List<Pokemon>> searchPokemon(@Query("name") String name, @Query("type") String type);

    @GET("pokemon/detail")
    Call<Pokemon> detailPokemon(@Query("id") int id);

}