package edu.nd.pmcburne.hwapp.one.network

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://ncaa-api.henrygd.me/"

/**
 * Retrofit service for NCAA Basketball scores.
 */
interface NcaaApiService {
    @GET("scoreboard/basketball-{gender}/d1/{year}/{month}/{day}")
    suspend fun getScoreboard(
        @Path("gender") gender: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String
    ): ScoreboardDto
}

/**
 * Singleton object to provide the Retrofit service.
 */
object NcaaApi {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val retrofitService: NcaaApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .build()
            .create(NcaaApiService::class.java)
    }
}
