package learn.tutorials.advance.jaxrs.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Path Params with member variables can not be used for singleton resources
 * 
 * @author KD
 *
 */
@Path("{pathparam}/demo")
public class MyResourcePerRequest {
	@PathParam("pathparam")
	private String pathParam;
	@QueryParam("query")
	private String queryParam;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "Path Param is : " + pathParam + " & Query Param is : " + queryParam;
	}
}
