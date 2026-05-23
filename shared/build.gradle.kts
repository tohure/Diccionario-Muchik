import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room3)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}

// providers.fileContents() registra local.properties como input de Configuration Cache.
// Properties() directo no lo hace: cambios en el archivo no invalidarían el caché.
val localPropsProvider: Provider<Properties> =
    providers.fileContents(rootProject.layout.projectDirectory.file("local.properties"))
        .asText
        .map { text -> Properties().apply { load(text.reader()) } }

fun secret(envKey: String, propKey: String = envKey): String =
    providers.environmentVariable(envKey)
        .orElse(localPropsProvider.map { it.getProperty(propKey, "") }.orElse(""))
        .get()

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    android {
        namespace = "dev.tohure.muchik_dictionary.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true  // requerido por el linker de Xcode para embeber el framework en el bundle
            binaryOption("bundleId", "dev.tohure.muchik_dictionary.shared")
        }
    }

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.room3.runtime)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.datastore.preferences.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.ktor.client.darwin)
        }

        jvmMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.ktor.client.cio)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

buildkonfig {
    packageName = "dev.tohure.muchik_dictionary"
    defaultConfigs {
        buildConfigField(STRING, "SUPABASE_URL", secret("SUPABASE_URL"))
        buildConfigField(STRING, "SUPABASE_KEY", secret("SUPABASE_PUBLISHABLE_KEY"))
    }
}

dependencies {
    // Equivalente a debugImplementation con com.android.kotlin.multiplatform.library.
    // El plugin experimental no expone variantes de build en la forma estándar de AGP.
    androidRuntimeClasspath(libs.compose.uiTooling)

    // Room 3 KSP solo en targets nativos; js/wasmJs usan datos estáticos (worker WASM pendiente).
    // El expect object AppDatabaseConstructor usa @Suppress("NO_ACTUAL_FOR_EXPECT") para web.
    add("kspAndroid", libs.androidx.room3.compiler)
    add("kspIosArm64", libs.androidx.room3.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room3.compiler)
    add("kspJvm", libs.androidx.room3.compiler)
}

