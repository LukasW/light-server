package ch.smaug.light.server.rest;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * RestServer class.
 */
public final class RestServer {

	private static final String BASE_URI = "http://0.0.0.0:8080/light/api";

	private HttpServer httpServer;

	public void start() {
		final ResourceConfig rc = new ResourceConfig().packages(InfoRestService.class.getPackage().toString());
		httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	public void stop() {
		httpServer.shutdown();
	}
}
