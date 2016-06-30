node('ontrack') {
    // Mark the code checkout 'stage'....
    stage 'Checkout'

    // Get some code from a GitHub repository
    git url: 'https://github.com/nemerosa/ontrack.git', branch: 'build/slave'

    // Get the JDK
    def javaHome = tool name: 'JDK8u74', type: 'hudson.model.JDK'

    // Mark the code build 'stage'....
    stage 'Build'
    // Run the Gradle build and builds the Docker image
    sh """./gradlew \\
        clean \\
        versionDisplay \\
        versionFile \\
        test \\
        integrationTest \\
        osPackages \\
        build \\
        --info \\
        --stacktrace \\
        --profile \\
        --console plain \\
        --no-daemon \\
        -Dorg.gradle.java.home=${javaHome} \\
        -Dorg.gradle.jvmargs="-Xmx3072m"
        """
    // Archiving the tests
    step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/*.xml'])
    // TODO Ontrack build

    // Extracts the version from the file
    def versionInfo = readProperties file: 'build/version.properties'
    env.VERSION_DISPLAY = versionInfo.VERSION_DISPLAY
    echo "Version = ${env.VERSION_DISPLAY}"

    // Local acceptance tests
    stage 'Local acceptance'
    sh """./gradlew \\
        ciAcceptanceTest \\
        --info \\
        --profile \\
        --stacktrace
        --console plain \\
        --no-daemon \\
        -Dorg.gradle.java.home=${javaHome} \\
        -Dorg.gradle.jvmargs="-Xmx3072m"
        """
    // Archiving the tests
    step([$class: 'JUnitResultArchiver', testResults: '*-tests.xml'])
    // TODO Ontrack validation
}