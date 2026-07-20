package com.abaiaor.ultrasavenothing.atp.annotations

/** Marks a step definition method matching a Gherkin "Given" step. See docs/atp/conventions.md. */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Given(val pattern: String)
