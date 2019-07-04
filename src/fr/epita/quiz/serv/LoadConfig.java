package fr.epita.quiz.serv;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class LoadConfig {
	private static LoadConfig instance;
	
	private static final String defaultConfigFileLocation = "conf.properties";

	public static LoadConfig getInstance() {
		if (instance == null) {
			instance = new LoadConfig();
		}
		return instance;
	}
	
	Properties props = new Properties();
	boolean init = false;
	
	private LoadConfig() {
		// properties loading
		try {
			File confFile = new File("config\\Database.properties");
			FileInputStream ips = new FileInputStream(confFile);
			props.load(ips);
			init =true;
		} catch (Exception e) {
			init=false;
		}

	}

	public boolean isInit() {
		return init;
	}



	public String getConfigurationValue(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}
	
}
