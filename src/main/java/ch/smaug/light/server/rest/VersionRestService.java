package ch.smaug.light.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("version")
public class VersionRestService {

	@GET
	@Produces("text/plain")
	public String getInfoText() {
		return "Version: " + getClass().getPackage().getImplementationVersion();
	}
}
