plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

sourceSets {
    val p1 = create("P1") {
        java.srcDir("./src/P1")
    }

    create("P2") {
        java.srcDir("./src/P2")
        compileClasspath += configurations.testCompileClasspath
        runtimeClasspath += configurations.testRuntimeClasspath
    }

    val p3 = create("P3") {
        java.srcDir("./src/P3")
    }

    test {
        java.srcDir("./src/test")
    }

    create("P1Test") {
        java.srcDir("./test/P1")
        compileClasspath += configurations.testCompileClasspath + p1.output
        runtimeClasspath += configurations.testRuntimeClasspath + p1.output
    }

    create("P3Test") {
        java.srcDir("./test/P3")
        compileClasspath += configurations.testCompileClasspath + p3.output
        runtimeClasspath += configurations.testRuntimeClasspath + p3.output
    }


}

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_11
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
