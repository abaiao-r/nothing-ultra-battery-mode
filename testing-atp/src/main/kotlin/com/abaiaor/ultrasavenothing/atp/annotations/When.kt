package com.abaiaor.ultrasavenothing.atp.annotations

/** Marks a step definition method matching a Gherkin "When" step. See docs/atp/conventions.md. */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class When(val pattern: String)
