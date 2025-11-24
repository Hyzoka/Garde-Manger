package data.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    includes(platformModule())

    // Database
    single {
        val driver = get<DatabaseDriverFactory>().createDriver()
        AppDatabase(driver)
    }

    // Local data source
    singleOf(::ProductLocalDataSourceImpl) bind ProductLocalDataSource::class

    // HTTP client
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            defaultRequest {
                url("https://world.openfoodfacts.org/")
            }
        }
    }

    // Remote
    singleOf(::ProductDataSourceImpl) bind ProductDataSource::class
}

