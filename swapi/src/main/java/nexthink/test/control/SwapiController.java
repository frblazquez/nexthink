package nexthink.test.control;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/swapi") 
public class SwapiController {

	@Get
	@Produces(MediaType.TEXT_PLAIN)
	public String index() {
		return "Move to other place!";
	}
	
	@Get("/characters/{character}/starships")
	@Produces(MediaType.TEXT_PLAIN) 
	public String getStarshipsUsedByCharacter(String character) {
		return "Hello World: "+character; 
	}
	
	@Get("/planets/{planet}/inhabitants")
	@Produces(MediaType.TEXT_PLAIN) 
	public String getInhabitantsOfPlanet(String planet) {
		return "Hello World: "+planet; 
	}
}
