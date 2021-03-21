# Nexthink - Junior Software Engineer Test
	
**Francisco Javier Blázquez Martínez** \
**frblazquezm@gmail.com** \                     [comment]: # (TODO: beautify!)
**https://github.com/frblazquez** \             [comment]: # (TODO: beautify!)
**https://www.linkedin.com/in/frblazquez/** \   [comment]: # (TODO: beautify!)
\
Double degree in Mathematics - Computer Engineering \
\
Complutense University of Madrid, Spain \
École Politechnique Fédérale de Lausanne, Switzerland 



## Prerequisites

- [**Gradle**](https://gradle.org/)         [comment]: # (TODO: version?)
- [**Micronaut**](https://micronaut.io/)    [comment]: # (TODO: version?)
- [**Docker**](https://www.docker.com/)     [comment]: # (TODO: version?)



## Execution

Up to this point we have a microservice implemented in java with Micronaut framework and Gradle. To compile, test, create a docker image and deploy this we have to execute the following:

```
# Run the tests
> ./gradlew test

# Check the result of the tests
# open build/reports/tests/test/index.html

# Run the application (port 8080 by default)
# ./gradlew run

# Create a Docker image (returns an Image ID)
> ./gradlew dockerBuild

# Get the image ID returned and run the image as a container
docker run -p [host-port]:[container-port] [image ID]
```



## Technologies I have never used, first approach

#### Microservices

Microservices are an architectural and organizational approach to software development where software is composed of small independent services that communicate over well-defined APIs. Microservices architectures make applications easier to scale and faster to develop, enabling innovation and accelerating time-to-market for new features.

https://microservices.io/ \
https://opensource.com/resources/what-are-microservices \
https://www.redhat.com/en/topics/microservices/what-are-microservices \
https://www.bmc.com/blogs/microservices-best-practices/ \
https://www.marlabs.com/blog-practices-in-microservices-implementation  \
https://www.ibm.com/cloud/learn/rest-apis

#### Gradle

Gradle is a build automation tool for multi-language software development. It controls the development process in the tasks of compilation and packaging to testing, deployment, and publishing.

https://gradle.org/ \
https://www.baeldung.com/gradle \
https://www.tutorialspoint.com/gradle/index.htm 


#### Docker

Docker is an open source tool designed to make it easier to create, deploy, and run applications by using containers. Containers allow a developer to package up an application with all of the parts it needs, such as libraries and other dependencies, and deploy it as one package. By doing so, thanks to the container, the developer can rest assured that the application will run on any other Linux machine regardless of any customized settings that machine might have that could differ from the machine used for writing and testing the code.

https://opensource.com/resources/what-docker \
https://docs.docker.com/ \
https://www.atlassian.com/blog/software-teams/deploy-java-apps-with-docker-awesome \
https://runnable.com/docker/java/dockerize-your-java-application \
https://linuxhandbook.com/remove-docker-images/


#### SW-API

Star Wars API for retrieving data of the characters, starships or planets that appear in the films. 

Usage examples from the [documentation](https://swapi.dev/documentation):

```
# Query some data
> curl https://swapi.dev/api/people/1
```

```
# Search for some entry
> curl https://swapi.dev/api/people/?search=r2
```
 
```
# See the schema of some category
> curl https://swapi.dev/api/people/schema 
```

https://swapi.dev/ \
https://github.com/maartendekker1998/StarWarsAPI \
https://medium.com/swlh/getting-json-data-from-a-restful-api-using-java-b327aafb3751 \
https://stackoverflow.com/questions/45259521/how-to-get-json-data-from-this-api 


#### cURL

cURL is a command line tool that lets you create network requests.

https://curl.se/docs/ \
https://flaviocopes.com/http-curl/ \
https://hackernoon.com/how-to-easily-use-curl-for-http-requests-db3249c5d4e6 


#### Kubernetes

Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications.

https://kubernetes.io/ \
https://docs.oracle.com/en/solutions/deploy-microservices/index.html#GUID-3BB86E87-11C6-4DF1-8CA9-1FD385A9B9E9 \
https://docs.docker.com/get-started/kube-deploy/ \
https://medium.com/the-resonant-web/kubernetes-in-practice-part-2-2d2a7290dd65 \
https://octopus.com/blog/ultimate-guide-to-k8s-microservice-deployments \
https://octopus.com/docs/guides/deploy-java-docker-app/to-k8s/using-octopus-onprem-jenkins-dockerhub


#### Micronaut

Micronaut is designed to make creating microservices quick and easy. It is a JVM-based framework for building lightweight, modular applications. 

https://micronaut.io/ \
https://docs.micronaut.io/latest/guide/index.html \
https://guides.micronaut.io/creating-your-first-micronaut-app/guide/index.html \
https://www.baeldung.com/micronaut \
https://medium.com/swlh/a-guide-to-building-a-micronaut-application-with-micronaut-data-support-e578aeea5dd6 \
https://dzone.com/articles/building-microservices-with-micronaut \
https://dzone.com/articles/a-quick-guide-to-microservices-with-the-micronaut


#### Distributed systems

A distributed system, also known as distributed computing, is a system with multiple components located on different machines that communicate and coordinate actions in order to appear as a single coherent system to the end-user.

https://blog.stackpath.com/distributed-system/ \
https://www.tutorialspoint.com/Distributed-Systems \
https://www.codemag.com/Article/1909071/Design-Patterns-for-Distributed-Systems \
https://martinfowler.com/articles/patterns-of-distributed-systems/ 



## Troubleshooting

* **SSL certificate expired**

This error might arise when accessing [SWAPI](https://swapi.dev/), wheter from a web browser or when querying some data to the API as in the following case:

```
user$> curl https://swapi.dev/api/people/?search=r2
curl: (60) SSL certificate problem: certificate has expired
More details here: https://curl.haxx.se/docs/sslcerts.html

curl failed to verify the legitimacy of the server and therefore could not
establish a secure connection to it. To learn more about this situation and
how to fix it, please visit the web page mentioned above.
```

The solution is establishing a new certificate for the API, what has to be done by the developers/maintainers or. Another temporarily solution is changing the date of our device so that it seems to it that the certificate is not expired. 



## References

- [Micronaut first app](https://guides.micronaut.io/creating-your-first-micronaut-app/guide/index.html)
- [Run docker image as container](https://docs.docker.com/language/nodejs/run-containers/)


