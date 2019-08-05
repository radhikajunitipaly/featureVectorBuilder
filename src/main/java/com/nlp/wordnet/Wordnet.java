package com.nlp.wordnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wordnet {
	
	private static final String COMMAND= "wn %s -h -synsn";
	public List getWordList(String input) throws IOException, InterruptedException {
		System.out.println(new Wordnet().getClass().getClassLoader().getResource(""));
		ProcessBuilder pb = new ProcessBuilder();
		System.out.println("command is : "+COMMAND+" : and input is : "+input);
		pb.command("CMD", "/c", String.format(COMMAND, input));
		Process process = pb.start();
		int errCode = process.waitFor();
		System.out.println("error code is : "+errCode);
		System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
		return output(process.getInputStream());
	}

	private List output(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		List<String> allMatches;
		try {
			ArrayList<String> arrayList=new ArrayList<>();
			br = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				sb.append(line + System.getProperty("line.separator"));
			}
			 allMatches = new ArrayList<String>();
			 Matcher m = Pattern.compile("=>.*")
			     .matcher(sb.toString());
			 while (m.find()) {
			   String test=m.group().replaceAll("[^A-Za-z ,]", "");
			   if(!test.trim().isEmpty())
				   allMatches.addAll(Arrays.asList(test.split(",")));
			 } 
			 allMatches.forEach(a-> System.out.println(a));
		} finally {
			br.close();
		}
		return allMatches;
	}
	
//	private static boolean hasSpecialChar(String input) {
//		Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
//		Matcher m = p.matcher(input);
//		return m.find();
//	}

}

