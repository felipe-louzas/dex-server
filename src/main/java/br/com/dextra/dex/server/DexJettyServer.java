package br.com.dextra.dex.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;

import br.com.dextra.dex.server.db.Database;
import br.com.neologictech.common.LogUtils;

public class DexJettyServer {

	private static final Logger LOG = LogUtils.getLogger();

	private final Integer port;
	private final Database database;

	private final Server server = new Server();

	public DexJettyServer(final Database database, final Integer port) {
		this.database = database;
		this.port = port;
	}

	public void bind() throws Exception {
		final ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		server.addConnector(connector);

		final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/svc/");

		final ResourceConfig config = new ResourceConfig();
		config.packages("br.com.dextra.dex.server.ws");
		config.register(new AbstractBinder() {

			@Override
			protected void configure() {
				bind(database).to(Database.class);
			}
		});

		final ServletHolder holderRest = new ServletHolder("ws-rest", new ServletContainer(config));
		servletContextHandler.addServlet(holderRest, "/*");

		final ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setWelcomeFiles(new String[] { "index.html", "mockup.html" });
		resourceHandler.setResourceBase("./webapp/");

		final HandlerList handlers = new HandlerList();
		handlers.addHandler(servletContextHandler);
		handlers.addHandler(resourceHandler);
		handlers.addHandler(new DefaultHandler());
		server.setHandler(handlers);

		server.start();
		server.join();
	}

	public void close() {
		try {
			server.stop();
			server.destroy();

		} catch (final Exception ex) {
			LOG.warn("Houve um erro finalizando o servidor Jetty", ex);
		}
	}

}
