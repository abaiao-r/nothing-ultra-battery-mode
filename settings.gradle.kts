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
