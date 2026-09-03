package com.eykel.shoplistmock.products.data.network

import com.eykel.shoplistmock.core.network.NetworkSimulationConfig
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.random.Random

/**
 * Stands in for a real backend: a genuine [MockEngine] plugged into the same Ktor pipeline a
 * production engine (OkHttp/Darwin) would use, answering `/v1/products` and
 * `/v1/products/{id}` from [ProductFixtures] instead of a socket. [config] simulates latency
 * and an error rate so the UI's loading/error states are exercised for real.
 */
internal fun productMockEngine(json: Json, config: NetworkSimulationConfig): MockEngine =
    MockEngine { request ->
        delay(Random.nextLong(config.latencyMillis.first, config.latencyMillis.last + 1))

        if (config.errorRate > 0f && Random.nextFloat() < config.errorRate) {
            return@MockEngine respondError(HttpStatusCode.InternalServerError)
        }

        val segments = request.url.encodedPath.trim('/').split('/')
        when {
            segments == listOf("v1", "products") ->
                respondJson(json, ProductListResponseDto(ProductFixtures.summaries(), ProductFixtures.all.size))

            segments.size == 3 && segments[0] == "v1" && segments[1] == "products" -> {
                val detail = ProductFixtures.find(segments[2])
                if (detail != null) respondJson(json, detail) else respondError(HttpStatusCode.NotFound)
            }

            else -> respondError(HttpStatusCode.NotFound)
        }
    }

private inline fun <reified T> MockRequestHandleScope.respondJson(json: Json, body: T) = respond(
    content = json.encodeToString(body),
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
)
