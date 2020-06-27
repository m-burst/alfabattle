object Dependencies {
    object Kotlin {
        const val version = "1.3.72"
    }

    const val ktlint = "com.pinterest:ktlint:0.37.1"

    object DockerPlugin {
        const val version = "6.4.0"
    }

    object OpenApiGenerator {
        const val version = "4.3.1"
    }

    object SpringKafka {
        const val version = "2.5.2.RELEASE"
        const val core = "org.springframework.kafka:spring-kafka:$version"
        const val test = "org.springframework.kafka:spring-kafka-test:$version"
    }

    object SpringBoot {
        const val version = "2.3.1.RELEASE"
        const val starterTest = "org.springframework.boot:spring-boot-starter-test:$version"
        const val starterJdbc = "org.springframework.boot:spring-boot-starter-jdbc:$version"
        const val starterJetty = "org.springframework.boot:spring-boot-starter-jetty:$version"
        const val starterWeb = "org.springframework.boot:spring-boot-starter-web:$version"
        const val starterWebsocket = "org.springframework.boot:spring-boot-starter-websocket:$version"
    }

    object SpringData {
        const val elasticsearch = "org.springframework.data:spring-data-elasticsearch:4.0.1.RELEASE"
    }

    const val postgresqlDriver = "org.postgresql:postgresql:42.2.14"

    object Exposed {
        const val version = "0.24.1"
        const val core = "org.jetbrains.exposed:exposed-core:$version"
        const val jdbc = "org.jetbrains.exposed:exposed-jdbc:$version"
        const val javaTime = "org.jetbrains.exposed:exposed-java-time:$version"
    }

    object Retrofit {
        const val version = "2.9.0"
        const val core = "com.squareup.retrofit2:retrofit:$version"
        const val converterJackson = "com.squareup.retrofit2:converter-jackson:$version"
    }

    object Netty {
        const val version = "4.1.50.Final"
        const val handler = "io.netty:netty-handler:$version"
    }

    object Jackson {
        const val version = "2.11.0"
        const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:$version"
    }

    object SpringIntegration {
        const val version = "5.3.1.RELEASE"
        const val stomp = "org.springframework.integration:spring-integration-stomp:$version"
    }

    object ApacheCommons {
        const val math3 = "org.apache.commons:commons-math3:3.6.1"
    }
}
