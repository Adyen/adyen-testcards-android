// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.spotless) apply false
}

tasks.register("printVersion") {
    description = "Validates and prints the project version from libs.versions.toml."
    group = "versioning"
    doLast {
        val versionName = libs.versions.versionName.get()
        val versionRegex = "^[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{1,2}(-(alpha|beta|rc)[0-9]{2})?\$".toRegex()
        if (!versionName.matches(versionRegex)) {
            throw GradleException("Error: Invalid version name format: '$versionName'. Does not match regex: ${versionRegex.pattern}")
        }
        println(versionName)
    }
}
