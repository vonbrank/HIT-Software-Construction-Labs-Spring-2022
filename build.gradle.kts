plugins {
    java
}

group = "com.vonbrank.Lab2"
version = "1.0-SNAPSHOT"

sourceSets {
    val p1 = create("P1") {
        java.srcDir("./src/P1")
        task<Jar>("jarP1") {
            archiveBaseName.set("P1")
            manifest.attributes("Main-Class" to "poet.Main")
            dependsOn(runtimeClasspath)
            from(runtimeClasspath)
        }
    }

    val p2 = create("P2") {
        java.srcDir("./src/P2")
        compileClasspath += p1.output
        runtimeClasspath += p1.output
        task<Jar>("jarP2") {
            archiveBaseName.set("P2")
            manifest.attributes("Main-Class" to "FriendshipGraph")
            dependsOn(runtimeClasspath)
            from(runtimeClasspath)
        }
    }

    create("P1Test") {
        java.srcDir("./test/P1")
        compileClasspath += configurations.testCompileClasspath + p1.output
        runtimeClasspath += configurations.testRuntimeClasspath + p1.output
    }

    create("P2Test") {
        java.srcDir("./test/P2")
        compileClasspath += configurations.testCompileClasspath + p1.output + p2.output
        runtimeClasspath += configurations.testRuntimeClasspath + p1.output + p2.output
    }
}

tasks["build"].dependsOn("jarP1", "jarP2")

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_10
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_10
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
