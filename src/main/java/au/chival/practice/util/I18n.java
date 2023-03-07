package au.chival.practice.util;

import org.bukkit.ChatColor;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static au.chival.practice.Main.JAVAPLUGIN;


public class I18n {

	private static final HashMap<String, String> CACHE = new HashMap<>();

	private static final HashMap<String, String> VARIABLES = new HashMap<>();

	private static ResourceBundle BUNDLE;

	static { init(); }

	private static void init() {
		try { // Load the resource bundle from JAVAPLUGIN data dir
			URL[] urls = new URL[] { JAVAPLUGIN.getDataFolder().toURI().toURL() };
			ClassLoader loader = new URLClassLoader(urls);

			BUNDLE = ResourceBundle.getBundle("messages", Locale.ENGLISH, loader);
		} catch (Exception e) { // Fall back and use default messages.properties from inside the jar
			JAVAPLUGIN.saveResource("messages.properties", false);
			BUNDLE = ResourceBundle.getBundle("messages");
		}

		// Load variables
		for (String key : BUNDLE.keySet())
			if (key.startsWith("var."))
				VARIABLES.put(key.substring(4), ChatColor.translateAlternateColorCodes('&', BUNDLE.getString(key)));
	}

	public static String get(String key) {
		try {
			return BUNDLE.getString(key);
		} catch (Exception e) {
			JAVAPLUGIN.getLogger().warning("Missing translation for key: " + key);
			return key;
		}
	}

	public static String colorize(String msg) {
		msg = ChatColor.translateAlternateColorCodes('&', msg);

		// Apply variables
		for (String key : VARIABLES.keySet())
			msg = msg.replace("{" + key + "}", VARIABLES.get(key));

		return msg;
	}

	public static String format(String key, Object... args) {
		if (args.length == 0 && CACHE.containsKey(key)) return CACHE.get(key);
		String msg = colorize(get(key));

		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) continue;
			msg = msg.replace("{" + i + "}", args[i].toString());
		}

		if (args.length == 0) CACHE.put(key, msg);
		return msg;
	}
}
