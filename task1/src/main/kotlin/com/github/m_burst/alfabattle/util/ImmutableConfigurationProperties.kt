package com.github.m_burst.alfabattle.util

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.core.annotation.AliasFor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ConstructorBinding
@ConfigurationProperties
annotation class ImmutableConfigurationProperties(
    @get:AliasFor(annotation = ConfigurationProperties::class, attribute = "prefix")
    val prefix: String
)
