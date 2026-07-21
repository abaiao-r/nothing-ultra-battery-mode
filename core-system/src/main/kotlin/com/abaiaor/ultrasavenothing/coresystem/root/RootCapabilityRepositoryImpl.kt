package com.abaiaor.ultrasavenothing.coresystem.root

import com.topjohnwu.superuser.Shell
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Real implementation of [RootCapabilityRepository] backed by [libsu](https://github.com/topjohnwu/libsu).
 *
 * [Shell.getShell] blocks while it spawns and probes a shell (running an actual `su` command
 * the first time, which triggers the Magisk/SuperSU superuser prompt if not already
 * granted/denied), so it's always run off the main thread here.
 */
@Singleton
class RootCapabilityRepositoryImpl @Inject constructor() : RootCapabilityRepository {

    override suspend fun isRootAvailable(): Boolean = withContext(Dispatchers.IO) {
        runCatching { Shell.getShell().isRoot }.getOrDefault(false)
    }
}
