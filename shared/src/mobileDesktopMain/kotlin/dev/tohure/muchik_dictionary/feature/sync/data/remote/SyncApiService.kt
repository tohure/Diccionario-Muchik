package dev.tohure.muchik_dictionary.feature.sync.data.remote

import dev.tohure.muchik_dictionary.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class SyncApiService(private val client: HttpClient) {

    private val baseUrl = "${BuildKonfig.SUPABASE_URL}/rest/v1/word_entry"
    private val apiKey = BuildKonfig.SUPABASE_KEY

    suspend fun fetchAll(): List<WordEntryDto> = client.get(baseUrl) {
        header("apikey", apiKey)
        header("Authorization", "Bearer $apiKey")
        parameter("select", "*")
        parameter("order", "updated_at.asc")
    }.body()

    suspend fun fetchSince(updatedAt: String): List<WordEntryDto> = client.get(baseUrl) {
        header("apikey", apiKey)
        header("Authorization", "Bearer $apiKey")
        parameter("select", "*")
        parameter("updated_at", "gte.$updatedAt")
        parameter("order", "updated_at.asc")
    }.body()
}
