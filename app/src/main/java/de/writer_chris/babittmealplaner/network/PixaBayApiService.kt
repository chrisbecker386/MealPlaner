package de.writer_chris.babittmealplaner.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.writer_chris.babittmealplaner.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://pixabay.com/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

interface PixaBayApiService {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): PixaBayPhotos
}

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object PixaBayApi {
    val retrofitService: PixaBayApiService by lazy {
        retrofit.create(PixaBayApiService::class.java)
    }
}