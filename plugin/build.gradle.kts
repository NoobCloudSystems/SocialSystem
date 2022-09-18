repositories {
    maven("https://jitpack.io")
}
dependencies {
    implementation(project(":api"))

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("com.google.code.gson:gson:2.9.1")

    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")

    compileOnly("com.github.NoobCloudSystems.NoobCloud:api:1.0-SNAPSHOT")

    implementation("com.github.Heliumdioxid.database-api:mysql:v1.0.0-rc1")
    implementation("mysql:mysql-connector-java:8.0.30")
}

tasks.shadowJar {
    archiveFileName.set("SocialSystem-${project.version}.jar")
}