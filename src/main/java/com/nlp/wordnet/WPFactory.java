package com.nlp.wordnet;

public class WPFactory {
	
	private static String OS = System.getProperty("os.name").toLowerCase();

	
	public WordNetProcess getObj() {
		
		if (isWindows()) {
			return new WindowsProcess();
		} else if (isMac()) {
			return new UnixProcess();
		} 

		return null;
	}
	
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}
	
	

	
	

}
