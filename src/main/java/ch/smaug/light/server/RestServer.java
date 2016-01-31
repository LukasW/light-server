package ch.smaug.light.server;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * RestServer class.
 */
public class RestServer {

	private static final String BASE_URI = "http://0.0.0.0:8080/light/";

	private HttpServer httpServer;

	public void start() {
		final ResourceConfig rc = new ResourceConfig().packages(LightRestService.class.getPackage().toString());
		httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	public void stop() {
		httpServer.shutdown();
	}
}
