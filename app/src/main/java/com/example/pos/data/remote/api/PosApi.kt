package com.example.pos.data.remote.api

import com.example.pos.data.remote.models.request.SessionRequest
import com.example.pos.data.remote.models.response.SessionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PosApi {
    @POST("sessions/start")
    suspend fun startSession(@Body request: SessionRequest): Response<SessionResponse>
    
    @PUT("sessions/{id}/end")
    suspend fun endSession(@Path("id") sessionId: String): Response<SessionResponse>
}