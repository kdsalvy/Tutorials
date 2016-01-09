package learn.tutorials.advance.jaxrs.resource;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("test")
@Singleton
public class MyResourceSingleton {

	private int count;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String testMethod(){
		count += 1;
		return "This method was called " + count + " time(s)";
	}
}
