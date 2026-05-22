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
    alias(libs.plugins.androidx.room)
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
    // Genera source sets intermedios estándar de KMP (iosMain, nativeMain, appleMain, etc.).
    // Sin esto, iosMain.get() no existe y el dependsOn de mobileDesktopMain falla.
    applyDefaultHierarchyTemplate()

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

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        // Source set intermedio para android + ios + jvm.
        // Room, Ktor y DataStore no tienen implementaciones JS/WASM, por lo que no pueden ir
        // en commonMain. jsMain y wasmJsMain no dependen de este conjunto: usan datos estáticos.
        val mobileDesktopMain by creating {
            dependsOn(commonMain.get())
        }

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
        }
        mobileDesktopMain.dependencies {
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.datastore.preferences.core)
        }

        androidMain {
            dependsOn(mobileDesktopMain)
            dependencies {
                implementation(libs.androidx.room.sqlite.wrapper)
                implementation(libs.koin.android)
                implementation(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependsOn(mobileDesktopMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        jvmMain {
            dependsOn(mobileDesktopMain)
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}

room {
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

    // Room KSP solo en los targets de mobileDesktopMain; js y wasmJs usan datos estáticos.
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

