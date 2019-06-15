package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.cloud.language.v1.Sentence;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class Agent3 extends Agent {
	private static final long serialVersionUID = 1L;

	public class Interfaz extends OneShotBehaviour { 
		private static final long serialVersionUID = 1L;
		private final MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchPerformative(ACLMessage.FAILURE));

		public void action() {
			ACLMessage message = this.myAgent.blockingReceive(mt);
			try {
				//Comprobar el tipo de mensaje failure o request: No se como hacerlo
				System.out.println("Agente3: Ha llegado el mensaje");
				if(message.getPerformative() == ACLMessage.REQUEST) {
					Object obj = message.getContentObject();
					//				System.out.println("Mensaje: " + obj);
					//INSERTAR CODIGO AGENTE 3: No se donde estï¿½ =)
					JSONArray result = list2JSON((ArrayList<Person>)obj);
				}
				else {
					System.out.println("Agente3: Ha llegado un mensaje de error");
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
				//Enviar error a la api
			}

		}
		public JSONArray list2JSON(List<Person> people){
			Iterator<Person> it1 = people.iterator();
			Person person; 
			Sentence sentence;
			JSONArray json = new JSONArray();
			HashMap<String, Object> personJSON; 
			HashMap<String,Object> sentences;

			while(it1.hasNext()) {
				person = it1.next();
				JSONArray sentencesJSON = new JSONArray();
				sentences = new HashMap<>();
				Iterator<Sentence> it2 = person.getSentences().iterator();
				while(it2.hasNext()) {
					sentence = it2.next();				
					sentences.put("content", sentence.getText().getContent());
					sentences.put("score", sentence.getSentiment().getScore());
					sentences.put("magnitude", sentence.getSentiment().getMagnitude());
					sentencesJSON.add(sentences);
				}
				
				personJSON = new HashMap<>();
				personJSON.put("name", person.getName());
				personJSON.put("lang", person.getLang());
				personJSON.put("score", person.getScore());
				personJSON.put("magnitude", person.getMagnitude());
				personJSON.put("sentences", sentencesJSON);
				json.add(personJSON);
			}
			return json;
		}

	}

	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		//Cambiar nombre
		sd.setName("Interfaz");
		sd.setType("Interfaz");
		dfd.addServices(sd);
		sd.setName("Error");
		sd.setType("Error");
		try {
			DFService.register( this, dfd);
			System.out.println("Agent3: Servicio publicado");
			addBehaviour(new Interfaz());
		} catch (FIPAException e) {
			//Enviar error a la api
			e.printStackTrace();
		}
	}
}
