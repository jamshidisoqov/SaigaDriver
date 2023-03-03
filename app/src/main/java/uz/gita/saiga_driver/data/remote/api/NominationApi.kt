package uz.gita.saiga_driver.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.gita.saiga_driver.data.remote.response.nomination.NominationResponse

// Created by Jamshid Isoqov on 2/24/2023
interface NominationApi {

    @GET("/reverse")
    suspend fun getNominationAddress(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "geojson"
    ): Response<NominationResponse>

}