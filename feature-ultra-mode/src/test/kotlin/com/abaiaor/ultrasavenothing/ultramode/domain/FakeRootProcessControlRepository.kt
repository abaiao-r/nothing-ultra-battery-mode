package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coredata.root.RootProcessControlRepository

class FakeRootProcessControlRepository : RootProcessControlRepository {

    var startCallCount = 0
    var stopCallCount = 0

    override fun start() {
        startCallCount++
    }

    override fun stop() {
        stopCallCount++
    }
}
