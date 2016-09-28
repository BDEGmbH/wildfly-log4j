package de.bdegmbh.logging.jul2log4j;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class JulToLog4jHandler extends Handler {
	
	/**
	 * Default constructor
	 */
	public JulToLog4jHandler() {
		ConfigurationSource source;
		
		try {
			source = new ConfigurationSource(this.getClass().getClassLoader().getResourceAsStream("log4j.xml"));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void publish(LogRecord record) {
		if (record.getLevel().equals(Level.OFF)) return;
		
		Logger log4j = getTargetLogger(record.getLoggerName());
		org.apache.logging.log4j.Level level = convertLeveltoLog4j(record.getLevel());
		log4j.log(level, toLog4jMessage(record), record.getThrown());
	}

	static Logger getTargetLogger(String loggerName) {
		return LogManager.getLogger(loggerName);
	}

	private String toLog4jMessage(LogRecord record) {
		String message = record.getMessage();
		// Format message
		try {
			Object parameters[] = record.getParameters();

			if (parameters != null && parameters.length != 0)
				message = MessageFormat.format(message, parameters);

		} catch (IllegalArgumentException ex) {
		}

		return message;
	}

	private org.apache.logging.log4j.Level convertLeveltoLog4j(Level level) {

		if (level.equals(Level.SEVERE)) {
			return org.apache.logging.log4j.Level.ERROR;
		} else if (level.equals(Level.WARNING)) {
			return org.apache.logging.log4j.Level.WARN;
		} else if (level.equals(Level.INFO)) {
			return org.apache.logging.log4j.Level.INFO;
		} else if (level.equals(Level.FINE)) {
			return org.apache.logging.log4j.Level.DEBUG;
		} else if (level.equals(Level.FINEST)) {
			return org.apache.logging.log4j.Level.TRACE;
		} else if (level.equals(Level.OFF)) {
			return org.apache.logging.log4j.Level.OFF;
		}

		return org.apache.logging.log4j.Level.OFF;
	}

	@Override
	public void close() throws SecurityException {
	}

	@Override
	public void flush() {
	}
}