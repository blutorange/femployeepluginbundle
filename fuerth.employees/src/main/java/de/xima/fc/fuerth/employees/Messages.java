package de.xima.fc.fuerth.employees;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Messages {
	private final static Logger LOG = LoggerFactory.getLogger(Messages.class);
	private static final String BUNDLE_NAME = "de.xima.fc.fuerth.employees.messages"; //$NON-NLS-1$

	private Messages() {
	}

	public static String msg(final String key, final Locale locale) {
		try {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(key);
		}
		catch (final MissingResourceException e) {
			LOG.error(String.format("Failed to get message for key %s.", key), e); //$NON-NLS-1$
			return "???" + key + "???"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static String fmt(final String key, final Locale locale, final Object... arguments) {
		final String msg = msg(key, locale);
		try {
			return MessageFormat.format(msg, arguments);
		}
		catch (final IllegalArgumentException e) {
			LOG.error(String.format("Failed to format %s with arguments %s.", msg, StringUtils.join(arguments), //$NON-NLS-1$
					Character.valueOf(',')), e);
			return msg;
		}
	}
}
