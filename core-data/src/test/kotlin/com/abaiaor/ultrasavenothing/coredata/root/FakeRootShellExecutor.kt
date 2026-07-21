package com.abaiaor.ultrasavenothing.coredata.root

import com.abaiaor.ultrasavenothing.coresystem.root.RootShellExecutor

class FakeRootShellExecutor : RootShellExecutor {

    val executedCommands: MutableList<String> = mutableListOf()

    override fun run(command: String): List<String> {
        executedCommands += command
        return emptyList()
    }
}
