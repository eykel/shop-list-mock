package com.eykel.shoplistmock.core.network

/**
 * Tunables for the fake backend (see `products/data/network/ProductMockEngine.kt`): how long a
 * request takes and how often it fails, so the UI's loading/error states are exercised for real
 * instead of only ever seeing the happy path.
 */
data class NetworkSimulationConfig(
    val latencyMillis: LongRange = 400L..900L,
    val errorRate: Float = 0f
)
