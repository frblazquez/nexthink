package nexthink.test.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
public class SwapiControllerTest {
	
    @Inject
    @Client("/")
    RxHttpClient client; 

    @Test
    public void testGetInhabitantsPlanet() {
    	String character = "luke";
    	
        HttpRequest<String> request = HttpRequest.GET("/swapi/characters/"+character+"/starships"); 
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("Hello World: "+character, body);
    }
    
    @Test
    public void testGetStarshipsCharacter() {
    	String planet = "pluto";
    	
        HttpRequest<String> request = HttpRequest.GET("/swapi/planets/"+planet+"/starships"); 
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("Hello World: "+planet, body);
    }

}
