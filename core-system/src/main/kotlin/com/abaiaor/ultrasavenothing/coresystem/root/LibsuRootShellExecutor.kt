package com.abaiaor.ultrasavenothing.coresystem.root

import com.topjohnwu.superuser.Shell
import javax.inject.Inject
import javax.inject.Singleton

/** Real implementation of [RootShellExecutor], backed by libsu's [Shell.cmd]. */
@Singleton
class LibsuRootShellExecutor @Inject constructor() : RootShellExecutor {

    override fun run(command: String): List<String> =
        runCatching { Shell.cmd(command).exec() }
            .getOrNull()
            ?.takeIf { it.isSuccess }
            ?.out
            .orEmpty()
}
