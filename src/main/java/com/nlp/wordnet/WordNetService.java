package com.nlp.wordnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class WordNetService {
	
	private static final WordNetService service=new WordNetService();
	
	private WordNetService() {
	}
	
	public static WordNetService getInstance() {
		return service;
	}
	
	public boolean isScalar(String input) {
		try {
			@SuppressWarnings("unchecked")
			List<String> list=new Wordnet().getWordList(input,"");
			return checkInMetrics(list);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private boolean checkInMetrics(List<String> list) {
		//System.out.println(buildMetricsString());
		String metricsString = buildMetricsString();
		for(String each:list) {
			if(metricsString.contains(each.trim().toLowerCase())) {
				//System.out.println("Matcing word "+each);
				return true;
			}
				
		}
		return false;
	}
	
	private String buildMetricsString() {
		StringBuilder sb=new StringBuilder();
		URL resource = this.getClass().getClassLoader().getResource("metrics");
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(resource.getFile())));
			String st;
			while ((st = br.readLine()) != null) 
			    sb.append(st.trim()).append(",");
			    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString().toLowerCase();
	}
	
	public static void main(String[] args) {
		System.err.println(WordNetService.getInstance().isScalar("size"));
	}
}
