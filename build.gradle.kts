plugins {
    id("java")
}

group = "org.everbuild.minecraftheads"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.minestom)
    testImplementation(libs.minestom)
    testImplementation(libs.slf4j.simple)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}