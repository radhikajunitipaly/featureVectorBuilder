package com.nlp.wordnet;

import java.util.List;

public class WindowsProcess extends WordNetProcess{

	@Override
	List process(String input, String type) {
		try {
			//System.out.println(new Wordnet().getClass().getClassLoader().getResource(""));
			ProcessBuilder pb = new ProcessBuilder();
			pb.command("CMD", "/c", String.format(PropertyHelper.getWindowsPath(), input));
			Process process = pb.start();
			int errCode = process.waitFor();
			//System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
			return getListFromInputStream(process.getInputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
