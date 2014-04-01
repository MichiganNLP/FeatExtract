package config;

import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
	
	public static Configuration loadConfig(){
		try {
			 URL configFile = Config.class.getClassLoader()
                   .getResource("Configuration");
			config = new PropertiesConfiguration(configFile);
			return config;
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return config;
	}
	private static Configuration config = loadConfig();
	private static String LIWC_FILE = "liwc.file";
	private static String OPINION_FINDER_FILE = "opinionFinder.file";
	private static String ROGET_FILE = "roget.file";
	private static String WNA_FILE = "wna.file";
	private static String UNIGRAM_FILE = "unigrams.file";
	private static String LIWC_EXPANDED_FILE= "liwc.expanded.file";

	
	public static String getLIWCExpandedFile() {
		return config.getString(LIWC_EXPANDED_FILE);
	}

	public static String getOpinionFinderFile() {
		return config.getString(OPINION_FINDER_FILE);
	}

	public static String getRogetFile() {
		return config.getString(ROGET_FILE);
	}

	public static String getWnaFile() {
		return config.getString(WNA_FILE);
	}

	public static String getLiwcFile() {
		return config.getString(LIWC_FILE);
	}
	

	public static String getUnigramFile() {
		return config.getString(UNIGRAM_FILE);
	}


}
