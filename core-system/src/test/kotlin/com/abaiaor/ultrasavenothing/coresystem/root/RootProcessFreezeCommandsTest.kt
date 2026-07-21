package com.abaiaor.ultrasavenothing.coresystem.root

import org.junit.Assert.assertEquals
import org.junit.Test

class RootProcessFreezeCommandsTest {

    @Test
    fun `WHEN building force-stop command THEN it uses am force-stop with the package name`() {
        assertEquals(
            "am force-stop com.example.app",
            RootProcessFreezeCommands.forceStopCommand("com.example.app"),
        )
    }
}
