package com.abaiaor.ultrasavenothing.atp.annotations

/** Marks a step definition method matching a Gherkin "But" step. See docs/atp/conventions.md. */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class But(val pattern: String)
