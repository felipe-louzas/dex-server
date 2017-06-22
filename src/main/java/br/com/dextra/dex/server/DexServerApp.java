package br.com.dextra.dex.server;

import org.slf4j.Logger;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.db.sample.SampleDbData;
import br.com.dextra.dex.server.db.sample.SimpleMemoryDb;
import br.com.neologictech.common.LogUtils;

public class DexServerApp {
	private static final int DEFAULT_PORT_NUMBER = 8080;

	private static final Logger LOG = LogUtils.getLogger();

	public static void main(final String[] args) {

		final Database database = new SimpleMemoryDb();

		final SampleDbData sampleData = new SampleDbData();
		sampleData.load(database);

		final Integer portNumber = (args.length > 0) ? Integer.parseInt(args[0]) : DEFAULT_PORT_NUMBER;

		final DexJettyServer server = new DexJettyServer(database, portNumber);

		try {
			server.bind();

		} catch (final Exception ex) {
			LOG.error("Houve um erro iniciando o servidor.", ex);

		} finally {
			server.close();
		}
	}
}
