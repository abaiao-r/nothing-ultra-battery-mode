pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // JitPack hosts libsu (topjohnwu), used for root (su) shell access in Ultra Mode's
        // root-powered features. Not on Maven Central as of writing.
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "UltraSaveNothing"

include(":app")
include(":ui-components")
include(":feature-ultra-mode")
include(":feature-allowlist")
include(":feature-estimation")
include(":core-system")
include(":core-data")
include(":testing-atp")
