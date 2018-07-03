package debian.tomcat.mysql;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class DTMService {
	
	@GET
	public String testGet()
	{
		return "test GET ok";
	}

}
