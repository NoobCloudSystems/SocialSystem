# SocialSystem

SocialSystem is a [NoobCloud](https://github.com/NoobCloudSystems/NoobCloud) compatible friends and party system for
[Velocity](https://github.com/PaperMC/Velocity).

**Note**: This project is still in progress, there may be bugs and errors. At this time, I do not recommend using this
plugin on your production server.

## API
The API lets you access data from the SocialSystem and extend it.
The JavaDocs can be found [here](https://docs.luccboy.tk/socialsystem).

**Adding the API:**

Maven
```xml
<!-- Repositories -->
<repository>
    <id>luccboy-repo</id>
    <url>https://repo.luccboy.tk</url>
</repository>

<!-- Dependencies -->
<dependency>
    <groupId>xyz.luccboy.socialsystem</groupId>
    <artifactId>api</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

Gradle
```groovy
/* Repositories */
maven { url 'https://repo.luccboy.tk' }

/* Dependencies */
compile 'xyz.luccboy.socialsystem:api:1.0-SNAPSHOT'
```