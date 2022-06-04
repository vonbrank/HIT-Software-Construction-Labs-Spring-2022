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
//    sourceSets.forEach { println("${it.name} 的文件 是 ${it.java.srcDirs}") }

}

tasks["build"].dependsOn("main")

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_10
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_10
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
