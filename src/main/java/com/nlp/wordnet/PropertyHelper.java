package com.nlp.wordnet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHelper {
	
	private static final String FILE_PATH=PropertyHelper.class.getClassLoader()
											.getResource("config.properties").getFile();
	static Properties prop = new Properties();

	static {
		 try (InputStream input = new FileInputStream(FILE_PATH)) {
	            prop.load(input);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	}
	
	public static String getWindowsPath() {
		return prop.getProperty("windows");
		
	}
	
	public static String getUnixPath() {
		return prop.getProperty("unix");
		
	}
	
	public static void main(String[] args) {
		System.out.println(getUnixPath());
	}
	
}
