package agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.language.v1.*;

public class Person implements Serializable{

	private String name;
	private String lang;
	private float score;
	private float magnitude;
	private List<Sentence> sentences;	

	public Person(String name, String lang, float score, float magnitude, List<Sentence> sentences) {
		this.name = name;
		this.lang = lang;
		this.score = score;
		this.magnitude =magnitude;
		this.sentences = sentences;

	}


	public String getName() {
		return name;
	}

	public String getLang() {
		return lang;
	}

	public float getScore() {
		return score;
	}

	public float getMagnitude() {
		return magnitude;
	}


	public List<Sentence> getSentences() {
		return sentences;
	}

}