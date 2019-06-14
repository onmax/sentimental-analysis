package agents;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
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
				HashMap<String, String> messages = new HashMap<String, String>();
				String str = "Esto es un mensaje sin sentimiento. Odio tener que ir a misa. Te amo. Que bonito día. Quiero mucho a mi mama";
				messages.put("Alex", str);

				//		transformacion(messages);

				//		getData(messages);
				transformacion(getData(messages));
				
				//Envio de la información(obj) al agent3
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				AID receiver = getAddress("Interfaz");
				request.addReceiver(receiver);
				request.setContentObject((Serializable)obj);
				this.myAgent.send(request);
				System.out.println("Agente2: Mensaje enviado");

			}
			//EN EL CASO DE QUE SE PRODUZCA UN ERROR ENVIAR UN FAILURE AL AGENT3: Hay que limpiarlo
			catch (IOException e) {
				System.out.println("No existe el fichero");
				AID receiver = getAddress("Error");
				ACLMessage request = new ACLMessage(ACLMessage.FAILURE);
				request.addReceiver(receiver);
				request.setContent("Cambiar este texto por el objeto a enviar");
				this.myAgent.send(request);
				System.out.println("Agente1: Mensaje enviado");
				
			} catch (UnreadableException e) {
				System.out.println("No existe el fichero");
				AID receiver = getAddress("Error");
				ACLMessage request = new ACLMessage(ACLMessage.FAILURE);
				request.addReceiver(receiver);
				request.setContent("Cambiar este texto por el objeto a enviar");
				this.myAgent.send(request);
				System.out.println("Agente1: Mensaje enviado");
				
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

		public  JSONArray transformacion(List<Person>person){

			Iterator<Person> it = person.iterator();
			Person persona = null; 
			Sentence mensajes = null;
			JSONArray json = new JSONArray();
			JSONObject jsonObj = new JSONObject();
			JSONObject jsonMsg = new JSONObject();
			HashMap<String, Object> elem = new HashMap<>(); 
			HashMap<String,Object> mensaj = new HashMap<>();
			
			while(it.hasNext()) {
				persona = it.next();
				JSONArray jsonMsgs = new JSONArray();
				elem.put("name", persona.getName());
				elem.put("lang", persona.getLang());
				elem.put("score", persona.getScore());
				elem.put("magnitude", persona.getMagnitude());
				
				
				
				Iterator<Sentence> it2 = persona.getSentences().iterator();
				while(it2.hasNext()) {
					mensajes = it2.next();				
					mensaj.put("content", mensajes.getText().getContent());
					mensaj.put("score", mensajes.getSentiment().getScore());
					mensaj.put("magnitude", mensajes.getSentiment().getMagnitude());
					jsonMsgs.add(mensaj);
			
				}
				elem.put("sentences", jsonMsgs);
				json.add(elem);

			}
			System.out.println(json);
			return json;

		}


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
