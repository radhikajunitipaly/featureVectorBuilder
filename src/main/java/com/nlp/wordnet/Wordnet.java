package com.nlp.wordnet;

import java.io.IOException;
import java.util.List;

public class Wordnet {
	
	public List getWordList(String input,String type) throws IOException, InterruptedException {
		return new WPFactory().getObj().process(input, type);
	}

}

