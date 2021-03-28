package nexthink.test.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
	public void getDataFromAPITest() {
		HttpRequest<String> request = HttpRequest.GET(SwapiController.SWAPI_URL + "/people/9/");
		String body = client.toBlocking().retrieve(request);

		assertNotNull(body);
		assertTrue(body.length() > 0);
	}

	@Test
	public void getStarshipsUsedByCharacterTest() throws ParseException {
		String character = "luke";

		HttpRequest<String> request = HttpRequest.GET("/swapi/characters/" + character + "/starships");
		String body = client.toBlocking().retrieve(request);

		JSONParser parser = new JSONParser();
		JSONObject answer = (JSONObject) parser.parse(body);

		assertNotNull(answer);
		assertEquals((String) answer.get("character_name"), "Luke Skywalker");

		JSONArray starships = (JSONArray) answer.get("starships_used");

		assertTrue(starships.size() == 2);
		assertTrue(starships.contains("X-wing"));
		assertTrue(starships.contains("Imperial shuttle"));
	}

	@Test
	public void getInhabitantsOfPlanetTest() throws ParseException {
		String planet = "alderaan";

		HttpRequest<String> request = HttpRequest.GET("/swapi/planets/" + planet + "/inhabitants");
		String body = client.toBlocking().retrieve(request);

		JSONParser parser = new JSONParser();
		JSONObject answer = (JSONObject) parser.parse(body);

		assertNotNull(answer);
		assertEquals((String) answer.get("planet_name"), "Alderaan");
		assertEquals((String) answer.get("population"), "2000000000");

		JSONArray residents = (JSONArray) answer.get("residents");

		assertTrue(residents.size() == 3);
		assertTrue(residents.contains("Leia Organa"));
		assertTrue(residents.contains("Bail Prestor Organa"));
		assertTrue(residents.contains("Raymus Antilles"));
	}

	@Test
	public void noReadTimeoutTest() throws ParseException {
		// For this test 11 GET request are done sequentially to the SWAPI within the
		// same HttpRequest to our microservice
		String planet = "tatooin";

		HttpRequest<String> request = HttpRequest.GET("/swapi/planets/" + planet + "/inhabitants");
		String body = client.toBlocking().retrieve(request);

		JSONParser parser = new JSONParser();
		JSONObject answer = (JSONObject) parser.parse(body);

		assertNotNull(answer);
		assertEquals((String) answer.get("planet_name"), "Tatooine");
		assertEquals((String) answer.get("population"), "200000");

		JSONArray residents = (JSONArray) answer.get("residents");

		assertTrue(residents.size() == 10);
		assertTrue(residents.contains("Luke Skywalker"));
		assertTrue(residents.contains("Anakin Skywalker"));
		assertTrue(residents.contains("Shmi Skywalker"));
		assertTrue(residents.contains("C-3PO"));
		assertTrue(residents.contains("Darth Vader"));
		assertTrue(residents.contains("Owen Lars"));
		assertTrue(residents.contains("Beru Whitesun lars"));
		assertTrue(residents.contains("R5-D4"));
		assertTrue(residents.contains("Biggs Darklighter"));
		assertTrue(residents.contains("Cliegg Lars"));
	}

}
