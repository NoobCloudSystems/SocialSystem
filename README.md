# SocialSystem

SocialSystem is a [NoobCloud](https://github.com/NoobCloudSystems/NoobCloud) compatible friends and party system for
[Velocity](https://github.com/PaperMC/Velocity).

**Note**: This project is still a work in progress, there may be bugs, errors or missing features. At this time, I do
not recommend using this plugin on your production server.

## API
The API lets you access data from the SocialSystem and extend it.
The JavaDocs can be found [here](https://noobcloudsystems.github.io/docs/socialsystem).

**Adding the API:**

Maven
```xml
<!-- Repositories -->
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<!-- Dependencies -->
<dependency>
    <groupId>com.github.NoobCloudSystems.SocialSystem</groupId>
    <artifactId>api</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

Gradle
```groovy
// Repositories
maven { url 'https://jitpack.io' }

// Dependencies
compile 'com.github.NoobCloudSystems.SocialSystem:api:1.0-SNAPSHOT'
```

## ToDo
- [ ] Settings via command (friend and party)