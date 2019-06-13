package test;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.*;

import java.io.IOException;
import java.util.*;

public class FuncionesAgente2 {
	public static Person getPersonSentiment(LanguageServiceClient client, String name, String messages) {
		Document doc = Document.newBuilder().setContent(messages).setType(Type.PLAIN_TEXT).build();
		AnalyzeSentimentResponse response = client.analyzeSentiment(doc);

		Sentiment sentiment = response.getDocumentSentiment();
		if (sentiment == null) {
			System.out.println("No sentiment found for " + name);
			return null;
		}
		
		String lang = response.getLanguage();
		float magnitude = sentiment.getMagnitude();
		float score = sentiment.getScore();
		List<Sentence> sentences = response.getSentencesList();
		
		Person p = new Person(name, lang, score, magnitude, sentences);
		return p;
	}
	
	public static void printPersonData(Person p) {
		System.out.println("-------------------------------");
		System.out.printf("Nombre: %s\nLang: %s\n", p.getName(), p.getLang());
		System.out.printf("score: %.5f\nmagnitude: %.5f\n\n", p.getScore(), p.getMagnitude());
		System.out.println("Sentences");
		for(Sentence s: p.getSentences()) {
			System.out.print("\t" + s.getText().getContent() + "\n");
			System.out.printf("\tscore: %.5f\n\tmagnitude: %.5f\n\n", s.getSentiment().getScore(), s.getSentiment().getMagnitude());
		}
		System.out.println("-------------------------------");
	}
	
	public static ArrayList<Person> getData(HashMap<String, String> messages) throws IOException {
		ArrayList<Person> data = new ArrayList<Person>();

		try (LanguageServiceClient client = LanguageServiceClient.create()) {
			for (Map.Entry<String, String> entry : messages.entrySet()) {
				Person p = getPersonSentiment(client, entry.getKey(), entry.getValue());
				printPersonData(p);
				data.add(p);
			}
		}
		
		return data;
	}

	public static void main(String... args) throws IOException {
		// Example HashMap
		HashMap<String, String> messages = new HashMap<String, String>();
		String str = "Esto es un mensaje sin sentimiento. Odio tener que ir a misa. Te amo. Que bonito día. Quiero mucho a mi mama";
		messages.put("Alex", str);
		
		getData(messages);
	}
}