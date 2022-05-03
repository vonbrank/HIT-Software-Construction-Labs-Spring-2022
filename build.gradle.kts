plugins {
    id("java")
}

group = "com.vonbrank.Lab-1"
version = "1.0"

sourceSets {
    val p1 = create("P1") {
        java.srcDir("./src/P1")
        task<Jar>("jarP1") {
            archiveBaseName.set("P1")
            manifest.attributes("Main-Class" to "MagicSquare")
            dependsOn(runtimeClasspath)
            from(runtimeClasspath)
        }
    }

    val p2 = create("P2") {
        java.srcDir("./src/P2")
        compileClasspath += configurations.testCompileClasspath
        runtimeClasspath += configurations.testRuntimeClasspath

        task<Jar>("jarP2") {
            archiveBaseName.set("P2")
            manifest.attributes("Main-Class" to "turtle.TurtleSoup")
            dependsOn(runtimeClasspath)
            from(runtimeClasspath)
        }
    }

    val p3 = create("P3") {
        java.srcDir("./src/P3")

        task<Jar>("jarP3") {
            archiveBaseName.set("P3")
            manifest.attributes("Main-Class" to "FriendshipGraph")
            dependsOn(runtimeClasspath)
            from(runtimeClasspath)
        }
    }

    test {
        java.srcDir("./src/test")
    }

    create("P1Test") {
        java.srcDir("./test/P1")
        compileClasspath += configurations.testCompileClasspath + p1.output
        runtimeClasspath += configurations.testRuntimeClasspath + p1.output
    }

    create("P2Test") {
        java.srcDir("./test/P2")
        compileClasspath += configurations.testCompileClasspath + p2.output
        runtimeClasspath += configurations.testRuntimeClasspath + p2.output
    }

    create("P3Test") {
        java.srcDir("./test/P3")
        compileClasspath += configurations.testCompileClasspath + p3.output
        runtimeClasspath += configurations.testRuntimeClasspath + p3.output
    }


}

tasks["build"].dependsOn("jarP1", "jarP2", "jarP3")

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_11
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree("lib") { include("*.jar") })
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
