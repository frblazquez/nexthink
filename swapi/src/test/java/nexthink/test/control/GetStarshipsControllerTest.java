package nexthink.test.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
public class GetStarshipsControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client; 

    @Test
    public void testHello() {
        HttpRequest<String> request = HttpRequest.GET("/swapi/starships"); 
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("Hello World 2", body);
    }
    
}