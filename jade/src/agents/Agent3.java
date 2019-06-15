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
					JSONArray result = transformacion((ArrayList<Person>)obj);
				}
				else {
					System.out.println("Agente3: Ha llegado un mensaje de error");
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
				//Enviar error a la api
			}

		}
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
