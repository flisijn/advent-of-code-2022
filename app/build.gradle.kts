plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
