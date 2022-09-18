plugins {
    java
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
    group = "xyz.luccboy.socialsystem"
    version = "1.0-SNAPSHOT"

    apply {
        plugin("java")
        plugin("maven-publish")
        plugin("com.github.johnrengelman.shadow")
    }

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(18))
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}