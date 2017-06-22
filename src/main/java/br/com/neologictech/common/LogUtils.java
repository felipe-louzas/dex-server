package br.com.neologictech.common;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogUtils {

	private static final Logger LOG = LoggerFactory.getLogger(LogUtils.class);

	private static boolean initialized = false;

	private LogUtils() {
		/*
		 * This is a static utility class
		 */
	}

	public static Logger getLogger() {
		if (!initialized) initialize();

		final StackTraceElement caller = getCaller();
		return LoggerFactory.getLogger(caller.getClassName());
	}

	public static Logger getMethodLogger() {
		if (!initialized) initialize();

		final StackTraceElement caller = getCaller();
		return LoggerFactory.getLogger(caller.getClassName() + "." + caller.getMethodName());
	}

	private static StackTraceElement getCaller() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		/*
		 * trace[0] = Thread.getStackTrace();
		 * trace[1] = LogUtils.getCaller();
		 * trace[2] = LogUtils.getXXXLogger();
		 * trace[3] = CALLER <--
		 */
		return stackTrace[3];
	}

	public static Object format(final String format, final Object... params) {
		return new FormattedObject(format, params);
	}

	private static void initialize() {
		final ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();

		final Class<?> clsLoggerContext;
		try {
			clsLoggerContext = Class.forName("ch.qos.logback.classic.LoggerContext");

			if (!clsLoggerContext.isAssignableFrom(loggerFactory.getClass())) {
				/*
				 * SLF4J not bound to Logback, so return
				 */
				return;
			}

			/*
			 * Choose logback configuration file
			 */
			final URL logbackConf;

			final File userDebugFile = new File("conf/logback-test.xml");
			final File userConfigFile = new File("conf/logback.xml");

			if (userDebugFile.canRead()) {
				logbackConf = userDebugFile.toURI().toURL();
				LOG.debug("Automatically configuring Logback with conf/logback-test.xml");
			} else if (userConfigFile.canRead()) {
				logbackConf = userConfigFile.toURI().toURL();
				LOG.debug("Automatically configuring Logback with conf/logback.xml");
			} else {
				LOG.debug("No logback configuration found.");
				return;
			}

			/*
			 * The context was probably already configured by default configuration rules, so reset it
			 */
			final Method reset = clsLoggerContext.getMethod("reset", (Class<?>[]) null);
			reset.invoke(loggerFactory, (Object[]) null);

			/*
			 * Configure using file chosen above
			 */
			final Class<?> clsJoranConfigurator = Class.forName("ch.qos.logback.classic.joran.JoranConfigurator");
			final Class<?> clsLogbackContext = Class.forName("ch.qos.logback.core.Context");
			final Method setContext = clsJoranConfigurator.getMethod("setContext", clsLogbackContext);
			final Method doConfigure = clsJoranConfigurator.getMethod("doConfigure", URL.class);

			final Object configurator = clsJoranConfigurator.newInstance();
			setContext.invoke(configurator, loggerFactory);
			doConfigure.invoke(configurator, logbackConf);

		} catch (final ClassNotFoundException ex) {
			/*
			 * Logback is not in the classpath so, continue
			 */

		} catch (final Exception ex) {
			/*
			 * Something went wrong during the configuring of Logback, so warn
			 */
			ex.printStackTrace();
			LOG.warn("Error configuring Logback!", ex);

		} finally {
			initialized = true;

		}
	}

	private static final class FormattedObject {

		private final String format;
		private final Object[] params;

		public FormattedObject(final String format, final Object[] params) {
			this.format = format;
			this.params = params;
		}

		@Override
		public String toString() {
			return String.format(format, params);
		}

	}
}
