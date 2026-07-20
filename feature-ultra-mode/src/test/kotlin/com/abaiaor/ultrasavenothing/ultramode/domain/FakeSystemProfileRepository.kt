package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.profile.SystemProfileRepository

/** In-memory fake of [SystemProfileRepository] for unit tests, tracking call order/counts. */
class FakeSystemProfileRepository : SystemProfileRepository {

    var applyUltraProfileCallCount: Int = 0
        private set
    var revertUltraProfileCallCount: Int = 0
        private set

    override fun applyUltraProfile() {
        applyUltraProfileCallCount++
    }

    override fun revertUltraProfile() {
        revertUltraProfileCallCount++
    }
}
