package com.abaiaor.ultrasavenothing.ultramode.domain

import com.abaiaor.ultrasavenothing.coresystem.launcher.LauncherRoleRepository

class FakeLauncherRoleRepository : LauncherRoleRepository {

    var requestLauncherRoleCallCount = 0
        private set
    var releaseLauncherRoleCallCount = 0
        private set

    override fun requestLauncherRole() {
        requestLauncherRoleCallCount++
    }

    override fun releaseLauncherRole() {
        releaseLauncherRoleCallCount++
    }
}
