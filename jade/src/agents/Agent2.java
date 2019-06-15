package agents;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.*;

import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Agent2 extends Agent {

	private static final long serialVersionUID = 1L;
	
	public class Analisis extends OneShotBehaviour { 
		private static final long serialVersionUID = 1L;
		//A Agent2 solo le tienen que llegar request, no failure
		private final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		
		public void action() {
			ACLMessage message = this.myAgent.blockingReceive(mt);
			try {
				Object obj = message.getContentObject();
//				Object obj = message.getContent();
				System.out.println("Agent: Ha llegado el mensaje");
				System.out.println("Mensaje: " + obj);
				
				//INSERTAR CÓDIGO DE FUNCIONES AGENTE 2
				
				// Example HashMap
				HashMap<String, String> messages = (HashMap<String,String>)obj;
//				String str = "Esto es un mensaje sin sentimiento. Odio tener que ir a misa. Te amo. Que bonito día. Quiero mucho a mi mama";
//				messages.put("Alex", str);

				//		transformacion(messages);

				ArrayList<Person> data = getData(messages);
//				transformacion(getData(messages));
				//Envio de la información(obj) al agent3
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				AID receiver = getAddress("Interfaz");
				send(receiver, "REQUEST", data);
			}
			//EN EL CASO DE QUE SE PRODUZCA UN ERROR ENVIAR UN FAILURE AL AGENT3: Hay que limpiarlo
			catch (IOException e) {
				System.out.println("Agente2: No existe el fichero");
				AID receiver = getAddress("Error");
				send(receiver, "FAILURE", null);
			} catch (UnreadableException e) {
				System.out.println("No existe el fichero");
				AID receiver = getAddress("Error");
				send(receiver, "FAILURE", null);
				
			}
		}
		public  Person getPersonSentiment(LanguageServiceClient client, String name, String messages) {
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
		public  void printPersonData(Person p) {
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

		public  ArrayList<Person> getData(HashMap<String, String> messages) throws IOException {
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

		//h2j

		

	}

	public AID getAddress (String service){
		AID res = new AID();
		DFAgentDescription template=new DFAgentDescription();
		ServiceDescription templateSd=new ServiceDescription();
		templateSd.setName(service);
		//Poned el nombre que querais
//		templateSd.setName("Interfaz");
//		templateSd.setType("Interfaz");
		template.addServices(templateSd);
		try {
			boolean encontrado = false;
			while(!encontrado) {
				DFAgentDescription [] results = DFService.search(this, template);
				if(results.length > 0) {
					System.out.println("Agent2: Encontrado el servicio");
					DFAgentDescription dfd = results[0];
					System.out.println("Nombre del agente: " + dfd.getName().getName());
					System.out.println("Direccion: " + dfd.getName().getAddressesArray()[0]);
					encontrado = true;
					res = dfd.getName();
				}
			}
		}
		catch (FIPAException e) {
			e.printStackTrace();
		}
		return res;
	}
	public void send(AID receiver, String tipo, Object content) {
		ACLMessage request = (tipo.equals("REQUEST")) ? new ACLMessage(ACLMessage.REQUEST): new ACLMessage(ACLMessage.FAILURE);
		request.addReceiver(receiver);
//		request.setContent("Cambiar este texto por el objeto a enviar");
		if(content != null) {
			try {
				request.setLanguage(new SLCodec().getName());
				request.setEnvelope(new Envelope());
				request.getEnvelope().setPayloadEncoding("ISO8859_1");
				request.setContentObject((Serializable)content);
			} catch (IOException e) {
				
			}
		}
		this.send(request);
		System.out.println("Agente2: Mensaje enviado");
	}

	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		//Poned el nombre que querais
		sd.setName("EnviarMensajes");
		sd.setType("Mensajes");
		dfd.addServices(sd);
		try {
			DFService.register( this, dfd);
			System.out.println("Agent2: Servicio publicado");
			addBehaviour(new Analisis());
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

}
