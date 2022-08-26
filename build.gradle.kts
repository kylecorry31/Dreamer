buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath(kotlin("gradle-plugin", version = "1.7.10"))
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

task("clean") {
    delete(rootProject.buildDir)
}
