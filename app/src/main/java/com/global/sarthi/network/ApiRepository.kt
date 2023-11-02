package com.global.sarthi.network

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ApiRepository
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val appApi: AppApi
) {

    suspend fun postData( data : Map<String, String>,
    ) : Boolean {
        val response =  appApi.postData(data)
        Log.e("API_RESPONSE", response.toString())
        Log.e("API_RESPONSE2", response.isSuccessful.toString())
        Log.e("INSIDE_API", "INSIDE_API")
        return response.isSuccessful
    }

    suspend fun postLocation(imei: String, data : Map<String, String>) : Boolean {
        val response =  appApi.postLocation(imei,data)
        Log.e("LOCATION_API_RESPONSE", response.toString())
        Log.e("LOCATION_API_RESPONSE2", response.isSuccessful.toString())
        Log.e("LOCATION_INSIDE_API", "INSIDE_API")
        return response.isSuccessful
    }
}