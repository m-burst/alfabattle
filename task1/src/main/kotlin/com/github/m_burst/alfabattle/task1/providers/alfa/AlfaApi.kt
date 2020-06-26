package com.github.m_burst.alfabattle.task1.providers.alfa

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.m_burst.alfabattle.task1.providers.alfa.atm.AlfaAtmApi
import com.github.m_burst.alfabattle.util.ImmutableConfigurationProperties
import com.github.m_burst.alfabattle.util.SslContextFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@ImmutableConfigurationProperties("application.alfa-api")
data class AlfaApiProperties(
    val clientId: String,
    val privateKey: String,
    val certificate: String,
    val baseUrl: String
)

class AlfaApiClient(
    atmApi: AlfaAtmApi
) : AlfaAtmApi by atmApi {

    companion object {
        fun create(
            properties: AlfaApiProperties,
            objectMapper: ObjectMapper
        ): AlfaApiClient {
            val sslContext = SslContextFactory.getSSLContext(
                privateKey = properties.privateKey,
                certificate = properties.certificate
            )
            val okHttpClient = OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, SslContextFactory.defaultTrustManager)
                .addInterceptor(buildAuthInterceptor(clientId = properties.clientId))
                .build()
            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(properties.baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
            return AlfaApiClient(
                atmApi = retrofit.create()
            )
        }

        private fun buildAuthInterceptor(clientId: String): Interceptor {
            return Interceptor { chain ->
                val withHeader = chain.request()
                    .newBuilder()
                    .header("X-IBM-Client-Id", clientId)
                    .build()
                chain.proceed(withHeader)
            }
        }

        private inline fun <reified T : Any> Retrofit.create() = create(T::class.java)
    }
}
