package com.e.bloctap2pay.nfc.network


import com.e.bloctap2pay.nfc.model.BaseApiResponse
import com.e.bloctap2pay.nfc.model.CardDebitRequest
import com.e.bloctap2pay.nfc.model.RemoteResponse
import retrofit2.Response
import retrofit2.http.*



interface BlocApiService {
    @POST("terminals/debit-card-api")
    suspend fun performCardDebit(   @Body cardDebitRequest: CardDebitRequest):Response<BaseApiResponse<RemoteResponse>>
}