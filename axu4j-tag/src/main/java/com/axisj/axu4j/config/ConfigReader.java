package com.axisj.axu4j.config;

import org.apache.commons.lang.StringUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * 설정(axu4j.xml)을 읽는 Singleton 객체 입니다.
 * /WEB-INF/classes/axu4j.xml 
 * 
 * @author HJ Park
 */
public class ConfigReader {
	private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
	private static final String defaultConfigFilename = "axu4j.xml";
	
	private static AXUConfig config  = null;
    private static long lastLoadTime = -1L;

	private ConfigReader() {}
	
	public static AXUConfig getConfig() {
		if (config == null || StringUtils.equalsIgnoreCase("DEV", config.getMode())) {
			load();
		}
		
		return config;
	}
	
	/**
	 * read config from confingPath
	 * 
	 * @param confingFilename like axu4j.xml
	 */
	public static void load(String confingFilename) {
		try {
			if (config == null) {
				config = new AXUConfig();
				logger.debug("create new AXUConfig instance");
			}

            // DEV 모드인 경우 각 태그마다 config를 요청하므로 3초에 한 번씩만 설정을 로딩하도록 한다.
            long nowTime = (new Date()).getTime();
            if (nowTime - lastLoadTime < 3000) {
                return;
            } else {
                lastLoadTime = nowTime;
            }

			Serializer serializer = new Persister();
			URL configUrl = config.getClass().getClassLoader().getResource(confingFilename);
			if (configUrl == null) {
				configUrl = ClassLoader.getSystemClassLoader().getResource(confingFilename);
			}
			File configFile = new File(configUrl.toURI());
			
			serializer.read(config, configFile);
			
            logger.info("load config from {}", configFile.getAbsolutePath());
            if (logger.isDebugEnabled()) {
				logger.debug("axu4j.xml\n{}", config);
			}
		} catch(Exception e) {
			logger.error("Fail to load axu4j.xml", e);
		}
	}
	
	/**
	 * read config from axu4j.xml
	 * 
	 */
	public static void load() {
		load(defaultConfigFilename);
	}
	
	public static void save(String confingFilename) throws Exception {
		Serializer serializer = new Persister();
		File configFile = new File(ClassLoader.getSystemClassLoader().getResource(confingFilename).toURI());
		serializer.write(config, configFile);
		
		logger.debug("save config to {}", configFile.getAbsolutePath());
	}
	
	public static void save() throws Exception {
		save(defaultConfigFilename);
	}
	
}
