#### Commands for local testing
1. Software Composition Analysis (SonaType)
```
mvn org.sonatype.ossindex.maven:ossindex-maven-plugin:audit   
```

2. Lint (Spotless)

Check linting issues
```
 mvn spotless:check
```
Auto-Correct linting issues
```
mvn spotless:apply
```

3. JaCoCo and Unit Test

JaCoCo report in **target/site/jacoco**
```
mvn clean verify
```