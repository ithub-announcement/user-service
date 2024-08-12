plugins {
  id("org.springframework.boot") version "3.3.2"
  id("io.spring.dependency-management") version "1.1.6"

  id("io.github.lognet.grpc-spring-boot") version "5.1.5"

  kotlin("jvm") version "1.9.24"
  kotlin("plugin.spring") version "1.9.24"
}

buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("io.github.lognet:grpc-spring-boot-starter-gradle-plugin:5.1.5")
  }
}

apply(plugin = "io.github.lognet.grpc-spring-boot")

group = "ru.itcollege"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  implementation("javax.xml.bind:jaxb-api:2.3.1")
  implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")

  // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt
  implementation("io.jsonwebtoken:jjwt:0.9.1")

  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.ldap:spring-ldap-core")
  implementation("org.springframework.security:spring-security-ldap")
  implementation("com.unboundid:unboundid-ldapsdk")

  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.3.0")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
