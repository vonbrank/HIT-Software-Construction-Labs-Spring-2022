plugins {
    java
}

group = "com.vonbrank"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java.srcDir("./src")
        task<Jar>("ElectionApp") {
            archiveBaseName.set("ElectionApp")
            manifest.attributes("Main-Class" to "app.ElectionApp")
            dependsOn(runtimeClasspath)
            from(runtimeClasspath)
        }
        task<Jar>("DinnerOrderApp") {
            archiveBaseName.set("DinnerOrderApp")
            manifest.attributes("Main-Class" to "app.DinnerOrderApp")
            dependsOn(runtimeClasspath)
            from(runtimeClasspath)
        }
        task<Jar>("BusinessVotingApp") {
            archiveBaseName.set("BusinessVotingApp")
            manifest.attributes("Main-Class" to "app.BusinessVotingApp")
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

tasks["build"].dependsOn("ElectionApp", "DinnerOrderApp", "BusinessVotingApp")

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_10
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_10
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
