pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "F1SetupInstructor"
include(":app")
include(":core:ui")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:data")
include(":domain")
include(":feature:home")
include(":feature:history")
include(":feature:chatbot")
include(":feature:results")
