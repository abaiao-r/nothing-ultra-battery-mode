package com.abaiaor.ultrasavenothing.coresystem.root

class FakeRootShellExecutor(
    private val outputsByCommand: Map<String, List<String>> = emptyMap(),
) : RootShellExecutor {

    val executedCommands: MutableList<String> = mutableListOf()

    override fun run(command: String): List<String> {
        executedCommands += command
        return outputsByCommand[command].orEmpty()
    }
}
