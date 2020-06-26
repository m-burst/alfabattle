package com.github.m_burst.alfabattle.task1.providers.alfa.atm

import retrofit2.http.GET
import com.github.m_burst.alfabattle.task1.providers.alfa.atm.model.JSONResponseBankATMDetails
import com.github.m_burst.alfabattle.task1.providers.alfa.atm.model.JSONResponseBankATMStatus
import retrofit2.Call

interface AlfaAtmApi {
    /**
     * Получение статической информации о банкоматах
     *
     * @return Call&lt;JSONResponseBankATMDetails&gt;
     */
    @GET("atm-service/atms")
    fun atmsGet(): Call<JSONResponseBankATMDetails>

    /**
     * Получение информации о доступности функций в банкоматах
     *
     * @return Call&lt;JSONResponseBankATMStatus&gt;
     */
    @GET("atm-service/atms/status")
    fun atmsStatusGet(): Call<JSONResponseBankATMStatus>
}
