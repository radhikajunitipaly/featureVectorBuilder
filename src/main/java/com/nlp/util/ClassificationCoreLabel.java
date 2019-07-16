package com.nlp.util;

public class ClassificationCoreLabel {
	
	private String word;
	private String pos;
	private String ner;
	
	public ClassificationCoreLabel() {
		super();
	}

	public ClassificationCoreLabel(String word, String pos, String ner) {
		super();
		this.word = word;
		this.pos = pos;
		this.ner = ner;
	}
	
	
	@Override
	public String toString() {
		return "CoreLabel [word=" + word + ", pos=" + pos + ", ner=" + ner + "]";
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getNer() {
		return ner;
	}

	public void setNer(String ner) {
		this.ner = ner;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}


}
