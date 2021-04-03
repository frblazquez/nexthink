# Nexthink - Software Engineer Test
	
**Francisco Javier Blázquez Martínez** \
\
Double degree in Mathematics - Computer Engineering \
\
Complutense University of Madrid, Spain \
École Politechnique Fédérale de Lausanne, Switzerland \
\
**frblazquezm@gmail.com**                      
**https://github.com/frblazquez**             
**https://www.linkedin.com/in/frblazquez/**   



## Introduction

I present here my implementation to the proposed design and functionality. I have followed all the optional requisites except the resources for deployment in a Kubernetes cluster. \
\
Thank you very much for your time but even more for the opportunity. \
Francis



## Prerequisites

- [**Gradle** (6.8.3)](https://gradle.org/)        
- [**Micronaut** (2.4.2)](https://micronaut.io/)   
- [**Docker** (19.03.8)](https://www.docker.com/)   



## Test, build and deploy

To compile, test and create a Docker image run a terminal in ```swapi``` folder and execute:
```
./gradlew  clean test dockerbuild
```
It will show the ID of the docker image created, having this we can execute the following command to deploy de application as a Docker container in port 6969:
```
docker run -p 6969:8080 [Docker image ID]
```



## Execution examples

Once the container is deployed, we can start getting Star Wars related information from our microservice by simply opening a terminal and creating http requests, these are some examples:
```
# Which starships drove Anakin Skywalker?
curl http://localhost:6969/swapi/characters/Anakin%20Skywalker/starships

# And Han Solo?
curl http://localhost:6969/swapi/characters/Han%20Solo/starships

# Which main caracters inhabited Alderaan?
curl http://localhost:6969/swapi/planets/Alderaan/inhabitants
```
\
We are internally using the search function provided by the API so we can also do request like the followings:
```
# This works, we don't have to specify the full name
curl http://localhost:6969/swapi/characters/vader/starships

# Also, it's not case sensitive!
curl http://localhost:6969/swapi/characters/dARth%20vAdER/starships

# If the name is ambiguous we log a warning and return one of the search matches
curl http://localhost:6969/swapi/characters/skywalker/starships

# And if the name doesn't mach any character we log a warning and return an error message
curl http://localhost:6969/swapi/characters/Homer%20Simpson/starships
```
\
The exectuion of all the previous commands is registered at the server side in the following way:
![Image](/swapi/imgs/execution_log.png) 



## Implementation design

For achieving the required functionality, I considered three different options:

##### 1.- Dump the API data to a Data Base

This way we have to take all the API data only once and we can solve the queries only with the help of our Data Base. This approach is feasible because of the small size of the data the API is handling. In this case our microservice would only be a DAO, being this the fastest solution (after paying the cost of creating the DB and filling it).

I have not implemented this solution mainly because I don't consider it the simpler one but also because it's not applicable to bigger, more opaque or more changeable APIs. 

##### 2.- Take the API information to data structures

Again this is feasible only because the data de API is handling has a small size (and small complexity). However, this would require the creation of big data structures and filling them with the data, executing several queries for this purpose each time the microservice is deployed, slowing down the service initialization and potentially consuming a lot of memory. 

##### 3.- Querying the API dynamically

This might not be the fastest approach, but it's the simplest and the one I decided to implement. Specially thanks to the ```search``` feature that the API includes. Each time we want to get information we simply do a request to the API. The only problem experienced is that some user request might require several request to the API. This is the case for example when asking for the inhabitants of a planet. The final user expects the inhabitant names and not API URLs, so first we do a search to get the planet and it's data and then we map the URLs of the inhabitant entries to names by repeatedly doing request to the API.

Of course a mixed approach using caches could also improve the performance of this implementation, however, these are not implemented (I didn't consider them worth for this only-for-test project). 



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

* **Error when casting JSON fields**

```
Internal Server Error: class <type1> cannot be cast to <type2>
```

This error was caused because, thinking that [Micronaut RxHttpClient](https://docs.micronaut.io/latest/api/io/micronaut/http/client/RxHttpClient.html) allowed to directly get the JSONObject, I was trying to get the answer to my API requests in the following way:
```
HttpRequest<?> request = HttpRequest.GET(path);
return client.toBlocking().retrieve(request, JSONObject.class);
```

The JSONObject was succesfully created but wasn't properly built. The solution was to parse it by hand:
```
HttpRequest<String> request = HttpRequest.GET(path);
String body = client.toBlocking().retrieve(request);

JSONParser parser = new JSONParser();
return (JSONObject) parser.parse(body);
```

* **Read timeout**

The [first API](https://swapi.dev/) was much slower than the [equivallent API](https://swapi.py4e.com/) finally used. This is why, when doing several requests to it, this error could appear. For avoiding this problem I modified the [Micronaut configuration file](swapi/src/main/resources/application.yml) to increase the client http read timeout limit. I also included an special test that requires several requests to assess that the limit is more than enough.  

* **Spaces in the names to query the API**

When getting parameters in the URI with ```@Get("/field/{parameter}")```, even if the spaces are expressed as ```%20``` the encoding is automatically changed. Therefore, if we want to use this parameter as part of an URL for a request we have to again replace the blank spaces by ```%20```.



## References

- [Micronaut User Guide](https://docs.micronaut.io/2.4.2/guide/index.html)
- [Micronaut API Reference](https://docs.micronaut.io/2.4.2/api/index.html)
- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)



