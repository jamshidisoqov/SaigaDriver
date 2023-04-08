package uz.gita.saiga_driver.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.gita.saiga_driver.data.remote.response.nomination.NominationResponse
import uz.gita.saiga_driver.data.remote.response.nomination.SearchResponse

// Created by Jamshid Isoqov on 2/24/2023
interface NominationApi {

    @GET("/reverse")
    suspend fun getNominationAddress(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "geojson"
    ): Response<NominationResponse>

    @GET("/search")
    suspend fun searchOrders(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 100
    ):Response<List<SearchResponse>>

}