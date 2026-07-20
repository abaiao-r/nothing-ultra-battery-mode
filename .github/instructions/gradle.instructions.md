---
applyTo: "**/*.gradle.kts, gradle/libs.versions.toml, settings.gradle.kts"
---

# Gradle

- Use the version catalog (`gradle/libs.versions.toml`) for all dependency versions. Never
  hardcode a version string directly in a module `build.gradle.kts`.
- Every module in the module baseline (`:app`, `:ui-components`, `:feature-*`, `:core-*`,
  `:testing-atp`) must be declared in `settings.gradle.kts` before being referenced elsewhere.
- Module `build.gradle.kts` ordering: plugins block, then `android {}` block, then
  `dependencies {}` block.
- Dependency ordering inside `dependencies {}`: project modules first (`implementation(project(":..."))`),
  then version-catalog libraries, then test dependencies.
- Feature modules depend on `:ui-components` for shared UI and on `:core-system`/`:core-data`
  through their own `UseCase`/`Repository` interfaces, never by reaching into another feature
  module's internals.
- `:testing-atp` and any module running ATP scenarios must apply the `-Patp` Gradle property
  convention documented in `docs/atp/README.md` (do not invent a different flag name).
