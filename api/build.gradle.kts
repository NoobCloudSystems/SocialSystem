dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
}

tasks.shadowJar {
    archiveFileName.set("SocialSystemAPI-${project.version}.jar")
}