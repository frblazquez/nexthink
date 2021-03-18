package nexthink.test.control;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;


@Controller("/swapi/inhabitants") 
public class GetInhabitantsController {

// TODO: How to specify we'll take an argument?
// TODO: Set @Produces to JSON, MediaType.APPLICATION_JSON ?
// TODO: Implement properly

	@Get 
	@Produces(MediaType.TEXT_PLAIN) 
	public String index() {
		return "Hello World 1"; 
	}
}
