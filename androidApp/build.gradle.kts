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

    // Empaqueta los recursos CMP del módulo shared en los assets del APK con el namespace correcto.
    // assembleJvmMainResources genera la estructura con namespace que Android Runtime requiere;
    // CopyResourcesToAndroidAssetsTask (el task nativo de CMP para este caso) es internal en el plugin
    // y no expone API pública para configurar su outputDirectory en setups multi-módulo.
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

afterEvaluate {
    listOf("mergeDebugAssets", "mergeReleaseAssets").forEach { taskName ->
        tasks.named(taskName) {
            dependsOn(project(":shared").tasks.named("assembleJvmMainResources"))
        }
    }
}