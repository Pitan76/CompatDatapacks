package net.pitan76.compatdatapacks;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompatDatapacks
{
	public static final String MOD_ID = "compatdatapacks76";
	public static final String MOD_NAME = "Compat Datapacks";

	public static void init() {
		OldRegistryKeys.init();
	}

	public static Logger LOGGER = LogManager.getLogger();

	public static void log(Level level, String message){
		LOGGER.log(level, "[" + MOD_NAME + "] " + message);
	}

	public static void log(String message){
		log(Level.INFO, message);
	}
}
