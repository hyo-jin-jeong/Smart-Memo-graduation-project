package com.kakao.smartmemo.ApiConnect

import com.squareup.otto.Bus

class BusProvider {
    private var BUS = Bus()

    fun getInstance(): Bus {
        return BUS
    }

    private fun BusProvider() {
        // No instances.
    }
}
