to build the project install build plugin [maven-assembly-plugin](https://maven.apache.org/plugins/maven-assembly-plugin/usage.html#execution-building-an-assembly)
and then run `mvn clean package` to build the project.
After that, there are will be created 2 jars in the `\target`, 
first is original jar without dependencies `"your-project-name"-1.0-SNAPSHOT.jar`, 
and the second fat jar`"your-project-name"-1.0-SNAPSHOT-jar-with-dependencies.jar`.
to build the .jar with `native-image` you should use `native-image -jar jar_name.jar`, also pay attention on `MANIFEST.MF` if there specified main class
