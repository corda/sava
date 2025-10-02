plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  implementation("com.jfrog.artifactory:com.jfrog.artifactory.gradle.plugin:6.0.1")
}
