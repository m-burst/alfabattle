import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Dependencies.SpringBoot.version apply false
    kotlin("jvm") version Dependencies.Kotlin.version apply false
    kotlin("plugin.spring") version Dependencies.Kotlin.version apply false
    id("com.bmuschko.docker-spring-boot-application") version Dependencies.DockerPlugin.version apply false
    id("org.openapi.generator") version Dependencies.OpenApiGenerator.version apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.gradle.idea")

    repositories {
        mavenCentral()
        jcenter()
    }

    val ktlintClasspath by configurations.creating

    dependencies {
        ktlintClasspath(Dependencies.ktlint)
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }

        val kotlinFiles = listOf("src/**/*.kt", "*.kts")

        register<JavaExec>("ktlint") {
            main = "com.pinterest.ktlint.Main"
            classpath = ktlintClasspath
            args(kotlinFiles)
        }

        register<JavaExec>("ktlintFormat") {
            main = "com.pinterest.ktlint.Main"
            classpath = ktlintClasspath
            args("-F")
            args(kotlinFiles)
        }
    }
}
