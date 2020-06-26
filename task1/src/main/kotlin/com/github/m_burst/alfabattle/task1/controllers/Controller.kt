package com.github.m_burst.alfabattle.task1.controllers

import com.github.m_burst.alfabattle.task1.providers.alfa.AlfaApiClient
import com.github.m_burst.alfabattle.task1.providers.alfa.atm.model.BankATMDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val alfaApiClient: AlfaApiClient
) {

    @GetMapping("/path")
    fun get(): BankATMDetails {
        val response = alfaApiClient.atmsGet().execute()
        val result = response.body()
        check(result != null && result.success == true) { "Request failed: $result ${response.errorBody()?.string()}" }

        return result.data!!
    }
}
