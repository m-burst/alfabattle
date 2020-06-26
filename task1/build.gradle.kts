import org.openapitools.generator.gradle.plugin.tasks.GenerateTask as OpenApiGenerateTask

plugins {
    application
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("com.bmuschko.docker-spring-boot-application")
    id("org.openapi.generator")
}

application {
    mainClassName = "com.github.m_burst.alfabattle.task1.ApplicationKt"
}

docker {
    springBootApplication {
        baseImage.set("openjdk:11")
    }
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation(Dependencies.SpringBoot.starterWebsocket)
    implementation(Dependencies.SpringBoot.starterJetty)
    implementation(Dependencies.Jackson.moduleKotlin)

    implementation(Dependencies.Retrofit.core)
    implementation(Dependencies.Retrofit.converterJackson)
    implementation(Dependencies.Netty.handler)

    implementation(Dependencies.Krossbow.stompJackson)

    testImplementation(Dependencies.SpringBoot.starterTest)
}

configurations.all {
    resolutionStrategy {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")

        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

val generateAlfaAtmClient by tasks.registering(OpenApiGenerateTask::class) {
    generatorName.set("java")

    inputSpec.set("$projectDir/src/specs/Сервис проверки статуса банкоматов.yaml")
    outputDir.set("$buildDir/generated-sources/alfa-atm/")
    apiPackage.set("com.github.m_burst.alfabattle.task1.providers.alfa.atm.api")
    modelPackage.set("com.github.m_burst.alfabattle.task1.providers.alfa.atm.model")
    configOptions.set(mapOf(
        "dateLibrary" to "java8",
        "library" to "retrofit2"
    ))
}

val generateAlfaAtmModel by tasks.registering(OpenApiGenerateTask::class) {
    generatorName.set("kotlin")

    inputSpec.set("$projectDir/src/specs/Сервис проверки статуса банкоматов.yaml")
    outputDir.set("$buildDir/generated-sources/alfa-atm/")
    apiPackage.set("com.github.m_burst.alfabattle.task1.providers.alfa.atm.api")
    modelPackage.set("com.github.m_burst.alfabattle.task1.providers.alfa.atm.model")
    configOptions.set(mapOf(
        "enumPropertyNaming" to "UPPERCASE",
        "apiSuffix" to "AlfaAtmApi",
        "library" to "jvm-retrofit2",
        "serializationLibrary" to "jackson"
    ))
}
