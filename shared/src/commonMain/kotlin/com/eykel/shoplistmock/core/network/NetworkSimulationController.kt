package com.eykel.shoplistmock.core.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Live-tunable version of [NetworkSimulationConfig]: the mock backend reads [config] on every
 * request (see `productMockEngine`), so latency/error rate can change while the app is running
 * — e.g. the list screen's "simulate failure" toggle — instead of being frozen at DI setup.
 */
class NetworkSimulationController(initial: NetworkSimulationConfig = NetworkSimulationConfig()) {

    private val _config = MutableStateFlow(initial)
    val config: StateFlow<NetworkSimulationConfig> = _config

    fun setAlwaysFail(enabled: Boolean) {
        _config.update { it.copy(errorRate = if (enabled) 1f else 0f) }
    }
}
