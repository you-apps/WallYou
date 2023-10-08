buildscript {
    val compose_version by extra("1.5.0-beta01")
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.5.0" apply false
    id("com.android.library") version "7.5.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}
