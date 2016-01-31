package ch.smaug.light.server;

import java.util.Arrays;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightServerDaemon implements Daemon {

	private final static Logger LOG = LoggerFactory.getLogger(LightServerDaemon.class);

	private final RestServer restServer = new RestServer();

	@Override
	public void init(final DaemonContext daemonContext) {
		LOG.info("Initialize daemon with " + Arrays.toString(daemonContext.getArguments()));
	}

	@Override
	public void start() {
		LOG.info("Starting daemon");
		restServer.start();
	}

	@Override
	public void stop() {
		LOG.info("Stopping daemon");
		restServer.stop();
	}

	@Override
	public void destroy() {
		LOG.info("Destroying daemon");
	}

	public static void main(final String[] args) {
		new LightServerDaemon().start();
	}
}
