package nexthink.test.control;

import java.util.ArrayList;
import java.util.List;

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

	private static final String SWAPI_URL = "https://swapi.dev/api";
	private static final Logger LOG = LoggerFactory.getLogger(SwapiController.class);
	
    @Inject
    @Client(SWAPI_URL)
    RxHttpClient client; 
    
    private JSONObject getDataFromAPI(String path) throws ParseException {
    	
    	// NOT WORKING! This doesn't properly parse the JSON object!!
    	//HttpRequest<?> request = HttpRequest.GET(path); 
        //return client.toBlocking().retrieve(request, JSONObject.class);
    	
    	HttpRequest<String> request = HttpRequest.GET(path); 
        String body = client.toBlocking().retrieve(request);
        
        JSONParser parser = new JSONParser();  
        return (JSONObject) parser.parse(body);  
    }
    
    
	@Get("/characters/{characterName}/starships")
	@Produces(MediaType.APPLICATION_JSON) 
	public JSONObject getStarshipsUsedByCharacter(String characterName) throws ParseException {
		
		JSONObject searchResult = getDataFromAPI("/people/?search="+characterName);
		JSONObject answer = new JSONObject();
        
		if ((long) searchResult.get("count") == 0) {
			LOG.warn("No result found in SWAPI for character name \""+characterName+"\"");
			answer.put("message", "Character \""+characterName+"\" not found");
			return answer;
		} else if ((long) searchResult.get("count") > 1) {
			LOG.warn("Several results found in SWAPI for character name \""+characterName+"\"");
		}
		
		JSONArray arr  = (JSONArray) searchResult.get("results");
		JSONObject characterJSON = (JSONObject) arr.get(0);
		List<String> starshipsUsedURLs = (List<String>) characterJSON.get("starships");
		List<String> starshipsUsedNames= new ArrayList<String>();
		
		for (String ss_url: starshipsUsedURLs) 
			starshipsUsedNames.add((String) getDataFromAPI(ss_url).get("name"));
		
		answer.put("character_name", characterJSON.get("name"));
		answer.put("starships_used", starshipsUsedNames);

		return answer;
	}
	
	@Get("/planets/{planetName}/inhabitants")
	@Produces(MediaType.APPLICATION_JSON) 
	public JSONObject getInhabitantsOfPlanet(String planetName) throws ParseException {
		
		JSONObject searchResult = getDataFromAPI("/planets/?search="+planetName);
		JSONObject answer = new JSONObject();
		
		if ((long) searchResult.get("count") == 0) {
			LOG.warn("No result found in SWAPI for character name \""+planetName+"\"");
			answer.put("message", "Planet \""+planetName+"\" not found");
			return answer;
		} else if ((long) searchResult.get("count") > 1) {
			LOG.warn("Several results found in SWAPI for planet name \""+planetName+"\"");
		}
		
		JSONArray arr  = (JSONArray) searchResult.get("results");
		JSONObject planetJSON = (JSONObject) arr.get(0);
		
		List<String> inhabitantsURLs = (List<String>) planetJSON.get("residents");
		List<String> inhabitantsNames= new ArrayList<String>();
		
		for (String inhabitants_url: inhabitantsURLs) 
			inhabitantsNames.add((String) getDataFromAPI(inhabitants_url).get("name"));
		
		answer.put("planet_name", planetJSON.get("name"));
		answer.put("population", planetJSON.get("population"));
		answer.put("residents", inhabitantsNames);
		
		return answer;
	}
}
