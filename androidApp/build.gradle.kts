import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}
dependencies {
    implementation(projects.shared)

    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "dev.tohure.muchik_dictionary"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    // WORKAROUND: el plugin `com.android.kotlin.multiplatform.library` (experimental de Google) no
    // engacha automáticamente con el pipeline de recursos de Compose Multiplatform (JetBrains).
    // CMP genera los assets en jvmMain con el namespace requerido por el Android Runtime, pero
    // no los copia al APK en este setup. Se elimina cuando alguno de los dos plugins resuelva
    // la integración nativa
    sourceSets.getByName("main").assets.srcDir(
        "${project(":shared").layout.buildDirectory.get()}/generated/compose/resourceGenerator/assembledResources/jvmMain"
    )

    defaultConfig {
        applicationId = "dev.tohure.muchik_dictionary"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

// AGP crea las tareas mergeAssets de forma lazy (no existen en tiempo de configuración).
// Sin afterEvaluate, tasks.named() fallaría. Sin el dependsOn, Gradle no garantiza
// que assembleJvmMainResources se ejecute antes que mergeAssets en un clean build.
afterEvaluate {
    listOf("mergeDebugAssets", "mergeReleaseAssets").forEach { taskName ->
        tasks.named(taskName) {
            dependsOn(project(":shared").tasks.named("assembleJvmMainResources"))
        }
    }
}