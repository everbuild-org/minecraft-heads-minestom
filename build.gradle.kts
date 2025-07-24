plugins {
    id("java")
    id("com.gradleup.nmcp.aggregation").version("1.0.1")
    `maven-publish`
    signing
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

tasks.register("determineVersion") {
    doLast {
        println(project.version)
    }
}

publishing.publications.create<MavenPublication>("maven") {
    groupId = project.group.toString()
    artifactId = project.name
    version = project.version.toString()

    from(project.components["java"])

    pom {
        name.set(this@create.artifactId)
        description.set("minecraft-heads.com API wrapper for Minestom")
        url.set("https://github.com/everbuild-org/minecraft-heads-minestom")

        licenses {
            license {
                name.set("MIT")
                url.set("https://github.com/everbuild-org/minecraft-heads-minestom/blob/master/LICENSE")
            }
        }

        developers {
            developer {
                id.set("Bloeckchengrafik")
                name.set("Christian Bergschneider")
            }
        }

        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/everbuild-org/minecraft-heads-minestom/issues")
        }

        scm {
            connection.set("scm:git:git://github.com/everbuild-org/minecraft-heads-minestom.git")
            developerConnection.set("scm:git:git@github.com:everbuild-org/minecraft-heads-minestom.git")
            url.set("https://github.com/everbuild-org/minecraft-heads-minestom")
            tag.set("HEAD")
        }

        ciManagement {
            system.set("Github Actions")
            url.set("https://github.com/everbuild-org/minecraft-heads-minestom/actions")
        }
    }
}

signing {
    isRequired = System.getenv("CI") != null

    val privateKey = System.getenv("GPG_PRIVATE_KEY")
    val keyPassphrase = System.getenv()["GPG_PASSPHRASE"]
    useInMemoryPgpKeys(privateKey, keyPassphrase)

    sign(publishing.publications)
}

nmcpAggregation {
    centralPortal {
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
        publishingType = "AUTOMATIC"
    }
    publishAllProjectsProbablyBreakingProjectIsolation()
}
