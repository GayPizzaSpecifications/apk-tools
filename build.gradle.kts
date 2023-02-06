import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  application

  kotlin("jvm") version "1.8.0"
  kotlin("plugin.serialization") version "1.8.0"

  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("org.graalvm.buildtools.native") version "0.9.19"
}

repositories {
  mavenCentral()
}

java {
  val javaVersion = JavaVersion.toVersion(17)
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-bom")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
  implementation("com.charleskorn.kaml:kaml:0.50.0")
  implementation("com.github.ajalt.clikt:clikt:3.5.1")

  implementation("io.ktor:ktor-client-core:2.2.3")
  implementation("io.ktor:ktor-client-cio:2.2.3")

  implementation("com.zaxxer:nuprocess:2.0.6")
  implementation("org.apache.commons:commons-compress:1.22")
  implementation("me.tongfei:progressbar:0.9.5")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

tasks.withType<Wrapper> {
  gradleVersion = "7.6"
}

application {
  mainClass.set("gay.pizza.pkg.apk.cli.MainKt")
}

graalvmNative {
  binaries {
    named("main") {
      imageName.set("apk-tools")
      mainClass.set("gay.pizza.pkg.apk.cli.MainKt")
      sharedLibrary.set(false)
    }
  }
}
