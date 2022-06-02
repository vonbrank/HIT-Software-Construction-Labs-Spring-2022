plugins {
    java
}

group = "com.vonbrank"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java.srcDir("./src")
        task<Jar>("main") {
            archiveBaseName.set("main")
            manifest.attributes("Main-Class" to "ElectionApp")
            dependsOn(runtimeClasspath)
            from(runtimeClasspath)
        }
    }

    test {
        java.srcDir("./test")
        compileClasspath += configurations.testCompileClasspath
        runtimeClasspath += configurations.testRuntimeClasspath
    }

}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
