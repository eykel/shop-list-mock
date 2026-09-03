package com.eykel.shoplistmock.products.data.network

import com.eykel.shoplistmock.core.network.NetworkError
import com.eykel.shoplistmock.core.network.NetworkSimulationConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface ProductApiService {
    suspend fun getProducts(): ProductListResponseDto
    suspend fun getProductDetail(id: String): ProductDetailDto
}

private const val BASE_URL = "https://api.shoplistmock.dev/v1"

/**
 * Talks to [BASE_URL] through a real Ktor pipeline (content negotiation + kotlinx.serialization)
 * whose engine is swappable at construction. [productApiService] wires [io.ktor.client.engine.mock.MockEngine]
 * so this repo has no real server to run; a production build would pass OkHttp/Darwin instead
 * and nothing above [ProductApiService] would need to change.
 */
internal class KtorProductApiService(private val client: HttpClient) : ProductApiService {

    override suspend fun getProducts(): ProductListResponseDto = get("$BASE_URL/products").body()

    override suspend fun getProductDetail(id: String): ProductDetailDto =
        get("$BASE_URL/products/$id").body()

    private suspend fun get(url: String): HttpResponse {
        val response = client.get(url)
        return when {
            response.status == HttpStatusCode.NotFound -> throw NetworkError.NotFound(url)
            response.status.value >= 500 -> throw NetworkError.Server(response.status.value)
            else -> response
        }
    }
}

fun productApiService(config: NetworkSimulationConfig = NetworkSimulationConfig()): ProductApiService {
    val json = Json { ignoreUnknownKeys = true }
    val client = HttpClient(productMockEngine(json, config)) {
        install(ContentNegotiation) { json(json) }
    }
    return KtorProductApiService(client)
}
