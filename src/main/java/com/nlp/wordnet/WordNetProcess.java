package com.nlp.wordnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class WordNetProcess {
	
	abstract List process(String input, String type);
	 
	 protected List getListFromInputStream(InputStream inputStream) throws IOException {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = null;
			List<String> allMatches;
			try {
				ArrayList<String> arrayList=new ArrayList<>();
				br = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;
				while ((line = br.readLine()) != null) {				
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
				 //allMatches.forEach(a-> System.out.println(a));
			} finally {
				br.close();
			}
			return allMatches;
		}

}
