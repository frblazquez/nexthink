package nexthink.test.control;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;

@Controller("/swapi")
public class SwapiController {

	public static final String SWAPI_URL = "https://swapi.py4e.com/api";
	private static final Logger LOG = LoggerFactory.getLogger(SwapiController.class);

	@Inject
	@Client(SWAPI_URL)
	RxHttpClient client;
	
	
	/**
	 * @param characterName Name of a Star Wars character
	 * @return JSON object with the name of all the starships driven by
	 *         <code>characterName</code>
	 */
	@Get("/characters/{characterName}/starships")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getStarshipsUsedByCharacter(String characterName) {

		LOG.info("Retrieving information of all the starships driven by character \"" + characterName + "\"");
		JSONObject responseToUser = new JSONObject();

		try {
			JSONObject characterJSON = querySearchToAPI("people", characterName);

			if (characterJSON == null) {
				responseToUser.put("message", "Character <" + characterName + "> not found");
			} else {
				JSONArray starshipsUsedNames = getNamesForURLs((JSONArray) characterJSON.get("starships"));
				responseToUser.put("character_name", characterJSON.get("name"));
				responseToUser.put("starships_used", starshipsUsedNames);
			}
		} catch (ParseException e) {
			LOG.error("Querying the starships used by \"" + characterName + "\" failed.");
			responseToUser.put("message", "Querying the starships used by <" + characterName + "> failed.");
		}
		return responseToUser;
	}

	
	/**
	 * @param planetName Name of a planet in the Star Wars universe
	 * @return JSON object with the name of the main characters that inhabit
	 *         <code>planetName</code>
	 */
	@Get("/planets/{planetName}/inhabitants")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getInhabitantsOfPlanet(String planetName) {

		LOG.info("Retrieving the name of the characters that inhabit the planet \"" + planetName + "\"");
		JSONObject responseToUser = new JSONObject();

		try {
			JSONObject planetJSON = querySearchToAPI("planets", planetName);

			if (planetJSON == null) {
				responseToUser.put("message", "Planet <" + planetName + "> not found");
			} else {
				JSONArray inhabitantsNames = getNamesForURLs((JSONArray) planetJSON.get("residents"));
				responseToUser.put("planet_name", planetJSON.get("name"));
				responseToUser.put("population", planetJSON.get("population"));
				responseToUser.put("residents", inhabitantsNames);
			}
		} catch (ParseException e) {
			LOG.error("Querying the inhabitants of \"" + planetName + "\" failed.");
			responseToUser.put("message", "Querying the inhabitants of <" + planetName + "> failed.");
		}

		return responseToUser;
	}
	
	
	// General purpose GET method for retrieven JSON Object from the API
	private JSONObject getDataFromAPI(String path) throws ParseException {

		// WARNING! This doesn't properly parse the JSON object!!
		// HttpRequest<?> request = HttpRequest.GET(path);
		// return client.toBlocking().retrieve(request, JSONObject.class);

		HttpRequest<String> request = HttpRequest.GET(path);
		String body = client.toBlocking().retrieve(request);

		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(body);
	}

	// Returns an element that matches the search or null if there is no such
	private JSONObject querySearchToAPI(String field, String name) throws ParseException {
		String nameForURL = name.replace(" ", "%20");
		JSONObject apiSearchResult = getDataFromAPI("/" + field + "/?search=" + nameForURL);

		if ((long) apiSearchResult.get("count") == 0) {
			LOG.warn("No result found in SWAPI for character name \"" + name + "\"");
			return null;
		} else if ((long) apiSearchResult.get("count") > 1) {
			LOG.warn("Several results found in SWAPI for character name \"" + name + "\"");
		}

		return (JSONObject) ((JSONArray) apiSearchResult.get("results")).get(0);
	}

	// Returns the name of the URLs given (we don't wont to return URL but
	// characters and starships names!)
	private JSONArray getNamesForURLs(JSONArray urls) throws ParseException {
		JSONArray names = new JSONArray();
		for (int i = 0; i < urls.size(); i++)
			names.add((String) getDataFromAPI((String) urls.get(i)).get("name"));
		return names;
	}
}
