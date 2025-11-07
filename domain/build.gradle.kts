plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)
    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)
}
