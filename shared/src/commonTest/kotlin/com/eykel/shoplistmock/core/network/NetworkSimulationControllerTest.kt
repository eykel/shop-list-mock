package com.eykel.shoplistmock.core.network

import kotlin.test.Test
import kotlin.test.assertEquals

class NetworkSimulationControllerTest {

    @Test
    fun setAlwaysFail_togglesErrorRateBetweenZeroAndOne() {
        val controller = NetworkSimulationController()

        controller.setAlwaysFail(true)
        assertEquals(1f, controller.config.value.errorRate)

        controller.setAlwaysFail(false)
        assertEquals(0f, controller.config.value.errorRate)
    }
}
