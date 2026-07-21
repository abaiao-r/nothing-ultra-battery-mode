package com.abaiaor.ultrasavenothing.coredata.root

/**
 * Periodically force-stops every installed app not on the current allowlist while Ultra Mode
 * is active, so they can't drain battery in the background even if the user never opens them.
 * Only has any real effect on a rooted device — see
 * [com.abaiaor.ultrasavenothing.coresystem.root.RootShellExecutor].
 */
interface RootProcessControlRepository {

    /** Starts the periodic force-stop sweep; safe to call again while already running. */
    fun start()

    /** Stops the periodic sweep. Does not un-stop already force-stopped apps. */
    fun stop()
}
