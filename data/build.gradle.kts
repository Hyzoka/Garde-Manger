plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()
   // iosX64()
   // iosArm64()
   // iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))

                // Kotlin Coroutines
                implementation(libs.coroutine.core)

                // Ktor multiplatform
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)

                // SQLDelight multiplatform
                implementation(libs.sqldelight.coroutines)

                // Koin multiplatform
                implementation(libs.koin.core)
            }
        }

        val androidMain by getting {
            dependencies {
                // Android-specific
                implementation(libs.androidx.core.ktx)
                implementation(libs.ktor.client.cio)
                implementation(libs.sqldelight.android)
                implementation(libs.koin.android)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
            }
        }
    }

    sqldelight {
        databases {
            create("ProductDataBase") {
                packageName.set("data.db")
            }
        }
    }
}

android {
    namespace = "data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}


