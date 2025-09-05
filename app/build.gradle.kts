import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    application
    checkstyle
    jacoco
    id("org.sonarqube") version "6.3.1.5724"
    id("io.freefair.lombok") version "8.14.2"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.19"

    id("com.gradleup.shadow") version "9.1.0"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("hexlet.code.App")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin:javalin-rendering:6.7.0")
    implementation("io.javalin:javalin-bundle:6.7.0")
    implementation("gg.jte:jte:3.2.1")
    implementation("org.slf4j:slf4j-simple:2.0.17")

    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("com.h2database:h2:2.3.232")
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("com.konghq:unirest-java-bom:4.5.1")
    implementation("com.konghq:unirest-java-core:4.5.1")
    implementation("com.konghq:unirest-modules-jackson:4.5.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("org.jsoup:jsoup:1.21.2")


    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.mockito:mockito-core:5.19.0")
    testImplementation("com.squareup.okhttp3:okhttp:5.1.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.1.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.projectlombok:lombok:1.18.40")
    annotationProcessor("org.projectlombok:lombok:1.18.40")
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }

checkstyle {
    toolVersion = "11.0.0"
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run

    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}

sonar {
    properties {
        property("sonar.projectKey", "wasiliyterkin46_java-project-72")
        property("sonar.organization", "wasiliyterkin46")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
