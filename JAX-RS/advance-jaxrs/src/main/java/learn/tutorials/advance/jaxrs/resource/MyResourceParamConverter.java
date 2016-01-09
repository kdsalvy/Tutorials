package learn.tutorials.advance.jaxrs.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import learn.tutorials.advance.jaxrs.model.MyDate;

@Path("date/{dateString}")
public class MyResourceParamConverter {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getDateString(@PathParam("dateString") MyDate date){
		return "DateString is " + date.toString();
	}
}
