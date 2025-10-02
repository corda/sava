pluginManagement {
  repositories {
    gradlePluginPortal()
    maven {
      url = uri("https://software.r3.com/artifactory/corda-dependencies")
      credentials {
        username = System.getenv("CORDA_ARTIFACTORY_USERNAME")
        password = System.getenv("CORDA_ARTIFACTORY_PASSWORD")
      }
    }
    mavenLocal()
  }
}

plugins {
  id("software.sava.build") version "0.1.36-j17-1"
}

rootProject.name = "sava"

javaModules {
  directory(".") {
    group = "software.sava"
    plugin("software.sava.build.java-module")
  }
}
