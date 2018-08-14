# overview
This is a solution for test project for "imageaccesscorp" company.
It contains implementation of the generic worker framework.
The [docs/assignment.txt](docs/assignment.txt) document contains the initial requirements.
The other _faq-xxx-q.txt_ and _faq-xxx-a.txt_ documents inside the [docs](docs) contin further clarifications of requirements.


# structure
The project is located inside [java-candidate-test](java-candidate-test) folder. It is implemented in Java, and has a standard Maven structure.
The build generates a _fat_ jar (containing all the dependencies) so it can easily be run from command line.

The framework code is in [java-candidate-test/src/main/java/com/agriniuk/imgaccorp/works](java-candidate-test/src/main/java/com/agriniuk/imgaccorp/works)

The demo code is in [java-candidate-test/src/main/java/com/agriniuk/imgaccorp/tempconvert](java-candidate-test/src/main/java/com/agriniuk/imgaccorp/tempconvert)

Of course, if you woud want to release this as a public framework, you'd want to separate the framework itself from the demo/examples code, 
but since this is a toy project which is not considered to be neede to anyone, it is simpler to embed the demo functionality inside the same jar.


# usage
First, go to source code folder
```
cd java-candidate-test
```

To build:
```
mvn clean package
```

To execute demo code:
```
java -cp target/imgaccorp-works-fat.jar com.agriniuk.imgaccorp.tempconvert.DemoApp
```

To generate javadoc (will be put into _target/site/apidocs/_):
```
mvn javadoc:javadoc
```


