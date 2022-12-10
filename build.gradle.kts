description = """Java Swing based thumbnail viewer (runs slow as hell)"""

plugins {
    id("java")
    id("application")
}

application {
    mainClass.set("com.djd.fun.thumbsup.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.5")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.inject:guice:5.1.0")
    implementation("com.google.inject.extensions:guice-assistedinject:5.1.0")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.swinglabs:swingx:1.6.1")
    implementation("net.coobird:thumbnailator:0.4.18")
    implementation("commons-codec:commons-codec:1.15")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.8.0")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.google.guava:guava-testlib:31.1-jre")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
