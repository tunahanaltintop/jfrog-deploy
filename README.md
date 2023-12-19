# jfrog-deployer

If you don't have access to the JFrog interface and you need to manually add dependencies from your Maven project to JFrog Artifactory, you can use the "jfrog-deploy" java application.

This maven project gathers dependencies in a local directory and then adds these dependencies to JFrog Artifactory using the "curl" command.

## The Background

The individual addition process can be done with the following command in the terminal:

<pre>
curl -X PUT -u username:password -T sample-jar-4.0.3.jar "http://your.artifactory/artifactory/libs-release-local/com/foo/sample-jar/4.0.3/sample-jar-4.0.3.jar"
</pre>

Here:

* The -u parameter specifies the authorized username and password for http://your.artifactory/artifactory.
* The -T parameter specifies the sample-jar-4.0.3.jar file in the current terminal directory.
* The URL indicates placing the sample-jar-4.0.3.jar file into the com/foo/sample-jar/4.0.3 directory under the libs-release-local repository.
* This command allows manually adding the dependency to JFrog Artifactory. If you want to automate this process, you may need to develop an automation tool.

This process should be done for the pom.xml file.

This way, in the Maven configuration using http://your.artifactory/artifactory, access to the library related to the following dependency consumption can be achieved:

    <dependency>
        <groupId>com.foo</groupId>
        <artifactId>sample-jar</artifactId>
        <version>4.0.3</version>
    </dependency>

This XML block defines the Maven dependencies for your project. The Group ID (groupId) represents a virtual group name, the Artifact ID (artifactId) specifies the name of a specific dependency in the project, and the Version (version) indicates the version number of the used dependency. This way, Maven can download and include the dependencies directly into your project from the specified location. In this case, the sample-jar library is set to version 4.0.3.

## The Solution

In this project, the following steps are performed:

### Overriding Maven Settings File:

Maven settings file can be overridden from IntelliJIDEA settings.
For example:

    /Users/youruser/.m2/settings-jfrog-deploy.xml. 

The content of the settings.xml file should be empty; otherwise, Maven will attempt to go to the http://your.artifactory/artifactory repository instead of the specified repository.

Sample settings.xml file:

    <?xml version="1.0" encoding="UTF-8"?>
    <settings xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd"
    xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    </settings>


### Overriding Maven Local Repository:

Maven local repository can be overridden from IntelliJIDEA settings.

For example:

    /Users/youruser/.m2/repository-jfrog-deploy. 

This directory will be the "PATH" value used in the "DeployLocalRepo2Jfrog.java" file.

### Specifying Dependencies or Plugins in pom.xml:

The dependencies or plugins to be used are specified in the pom.xml file, and a clean install is performed.

### Running DeployLocalRepo2Jfrog.java:

When the main method of "DeployLocalRepo2Jfrog.java" is executed, all dependencies under the /Users/youruser/.m2/repository-jfrog-deploy/ directory are recursively traversed, and they are deployed to the JFrog libs-release-local repository using the "curl" command mentioned above.

## Adding a New Dependency or Plugin:

When adding a new dependency or plugin, the previous dependencies in the pom.xml file should be either removed or commented out. After adding the new dependencies or plugins, the /Users/youruser/.m2/repository-jfrog-deploy/ directory should be deleted, and a clean install should be performed. Then, the running process mentioned in the above step should be applied.

This process appears to automate the deployment of dependencies to the JFrog libs-release-local repository and provides steps for updating libraries in the project.

