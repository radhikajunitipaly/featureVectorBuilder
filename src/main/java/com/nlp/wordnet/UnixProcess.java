package com.nlp.wordnet;

import java.util.List;

public class UnixProcess extends WordNetProcess{

	@Override
	List process(String input, String type) {
		try {
			//System.out.println(new Wordnet().getClass().getClassLoader().getResource(""));
			ProcessBuilder pb = new ProcessBuilder();
			pb.command("sh", "-c", String.format(PropertyHelper.getUnixPath(), input));
			Process process = pb.start();
			int errCode = process.waitFor();
			System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
			return getListFromInputStream(process.getInputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}

