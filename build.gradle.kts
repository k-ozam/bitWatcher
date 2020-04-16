// Top-level build file where you can add configuration options common to all sub-projects/modules.
import dependencies.Dep

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Dep.GradlePlugin.android)
        classpath(Dep.GradlePlugin.kotlin)
        classpath(Dep.GradlePlugin.kotlinSerialization)
        classpath(Dep.GradlePlugin.safeArgs)
        classpath(Dep.GradlePlugin.ossLicense)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}
