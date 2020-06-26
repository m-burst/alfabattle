package com.github.m_burst.alfabattle.util

import io.netty.handler.ssl.JdkSslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.SslProvider
import java.security.KeyFactory
import java.security.KeyStore
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SslContextFactory {

    private val log = logger<SslContextFactory>()

    fun getSSLContext(privateKey: String, certificate: String): SSLContext = getJdkSslContext(
        privateKey = privateKey,
        certificate = certificate
    ).context()

    val defaultTrustManager by lazy {
        val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        factory.init(null as KeyStore?)
        factory.trustManagers.filterIsInstance<X509TrustManager>().first()
    }

    private val trustManagerFactory by lazy {
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            .also { it.init(null as KeyStore?) }
    }

    private fun getJdkSslContext(
        privateKey: String,
        certificate: String
    ): JdkSslContext = SslContextBuilder.forClient()
        .sslProvider(SslProvider.JDK)
        .trustManager(trustManagerFactory)
        .setupClientAuth(privateKey, certificate)
        .build() as JdkSslContext

    private fun loadPrivateKey(privateKey: String): PrivateKey {
        val keyFactory = KeyFactory.getInstance("RSA")

        val base64 = privateKey.lines()
            .filter { !it.contains("-----BEGIN PRIVATE KEY-----") }
            .filter { !it.contains("-----END PRIVATE KEY-----") }
            .joinToString("") { it.trim() }

        val bytes = Base64.getDecoder().decode(base64)
        val keySpec = PKCS8EncodedKeySpec(bytes)
        return keyFactory.generatePrivate(keySpec).also {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val fingerprint = Base64.getEncoder().encodeToString(messageDigest.digest(bytes))
            log.info("Loaded RSA private key $fingerprint")
        }
    }

    private fun loadCertificate(certificate: String): X509Certificate {
        val stream = certificate.byteInputStream()
        return stream.use {
            CertificateFactory.getInstance("X.509")
                .generateCertificate(stream) as X509Certificate
        }.also {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val fingerprint = Base64.getEncoder().encodeToString(messageDigest.digest(it.encoded))
            log.info("Loaded X509 certificate $fingerprint")
        }
    }

    private fun SslContextBuilder.setupClientAuth(privateKey: String, certificate: String) = apply {
        keyManager(loadPrivateKey(privateKey), loadCertificate(certificate))
    }
}
