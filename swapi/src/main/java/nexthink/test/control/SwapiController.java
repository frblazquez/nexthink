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

	public  static final String SWAPI_URL = "https://swapi.py4e.com/api";
	private static final Logger LOG = LoggerFactory.getLogger(SwapiController.class);
	
    @Inject
    @Client(SWAPI_URL)
    RxHttpClient client; 
    
    private JSONObject getDataFromAPI(String path) throws ParseException {
    	
    	LOG.info("Querying: "+path);
    	
    	// WARNING! This doesn't properly parse the JSON object!!
    	//HttpRequest<?> request = HttpRequest.GET(path); 
        //return client.toBlocking().retrieve(request, JSONObject.class);
    	
    	HttpRequest<String> request = HttpRequest.GET(path); 
        String body = client.toBlocking().retrieve(request);
        
        JSONParser parser = new JSONParser();  
        return (JSONObject) parser.parse(body);  
    }
    
    /**
     * @param characterName Name of a Star Wars character
     * @return JSON object with the name of all the starships driven by <code>characterName</code>
     */
	@Get("/characters/{characterName}/starships")
	@Produces(MediaType.APPLICATION_JSON) 
	public JSONObject getStarshipsUsedByCharacter(String characterName) {
		
		LOG.info("Retrieving information of all the starships driven by character \""+characterName+"\"");
		JSONObject responseToUser  = new JSONObject();
		
		try {
			// Search query to the API
			JSONObject apiSearchResult = getDataFromAPI("/people/?search="+characterName);
			
			if ((long) apiSearchResult.get("count") == 0) {
				LOG.warn("No result found in SWAPI for character name \""+characterName+"\"");
				responseToUser .put("message", "Character <"+characterName+"> not found");
				return responseToUser ;
			} else if ((long) apiSearchResult.get("count") > 1) {
				LOG.warn("Several results found in SWAPI for character name \""+characterName+"\"");
			}
			
			JSONObject characterJSON = (JSONObject) ((JSONArray) apiSearchResult.get("results")).get(0);		
			JSONArray starshipsUsedURLs = (JSONArray) characterJSON.get("starships");
			JSONArray starshipsUsedNames= new JSONArray();
			for (int i = 0; i < starshipsUsedURLs.size(); i++) 
				starshipsUsedNames.add((String) getDataFromAPI((String) starshipsUsedURLs.get(i)).get("name"));
			
			responseToUser.put("character_name", characterJSON.get("name"));
			responseToUser.put("starships_used", starshipsUsedNames);
	
		} catch (ParseException e) {
			LOG.error("Querying the starships used by \""+characterName+"\" failed.");
			responseToUser.put("message", "Querying the starships used by <"+characterName+"> failed.");
		}
		return responseToUser;
	}
	
	/**
	 * @param planetName Name of a planet in the Star Wars universe
	 * @return JSON object with the name of the main characters that inhabit <code>planetName</code>
	 */
	@Get("/planets/{planetName}/inhabitants")
	@Produces(MediaType.APPLICATION_JSON) 
	public JSONObject getInhabitantsOfPlanet(String planetName) {
		
		LOG.info("Retrieving the name of the characters that inhabit the planet \""+planetName+"\"");		
		JSONObject responseToUser = new JSONObject();
		
		try {
			// Search query to the API
			JSONObject apiSearchResult = getDataFromAPI("/planets/?search="+planetName);
			
			if ((long) apiSearchResult.get("count") == 0) {
				LOG.warn("No result found in SWAPI for planet name \""+planetName+"\"");
				responseToUser.put("message", "Planet <"+planetName+"> not found");
				return responseToUser;
			} else if ((long) apiSearchResult.get("count") > 1) {
				LOG.warn("Several results found in SWAPI for planet name \""+planetName+"\"");
			}
			
			JSONObject planetJSON = (JSONObject) ((JSONArray) apiSearchResult.get("results")).get(0);
			JSONArray inhabitantsURLs = (JSONArray) planetJSON.get("residents");
			JSONArray inhabitantsNames= new JSONArray();
			for (int i = 0; i < inhabitantsURLs.size(); i++) 
				inhabitantsNames.add((String) getDataFromAPI((String) inhabitantsURLs.get(i)).get("name"));
			
			responseToUser.put("planet_name", planetJSON.get("name"));
			responseToUser.put("population",  planetJSON.get("population"));
			responseToUser.put("residents", inhabitantsNames);
			
		} catch(ParseException e) {
			LOG.error("Querying the inhabitants of \""+planetName+"\" failed.");
			responseToUser.put("message", "Querying the inhabitants of <"+planetName+"> failed.");
		}
		
		return responseToUser;
	}
}
