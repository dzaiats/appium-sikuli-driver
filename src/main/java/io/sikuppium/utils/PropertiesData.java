package io.sikuppium.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesData {

	private static final String PROP_FILE = "/application.properties";

	public static String getData(String name) {
		Properties props = new Properties();
		try {
			props.load(PropertiesData.class.getResourceAsStream(PROP_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String value = "";

		if (name != null) {
			value = props.getProperty(name);
		}
		return value;
	}
}
