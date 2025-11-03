package com.garde.data.di

import com.garde.data.datasources.remote.ProductDataSource
import com.garde.data.datasources.remote.ProductDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }, contentType = ContentType.Text.Plain)
            }

            defaultRequest {
                url("https://world.openfoodfacts.org/")
                contentType(ContentType.Application.Json)
            }
        }
    }


    @Provides
    @Singleton
    fun provideFilterRemoteDataSource(client: HttpClient): ProductDataSource {
        return ProductDataSourceImpl(client)

    }
}