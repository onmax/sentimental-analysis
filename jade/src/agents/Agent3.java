package agents;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;

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
				Object obj = message.getContentObject();
				RandomAccessFile file = new RandomAccessFile("output.json", "rw");
				file.setLength(0);
				if(message.getPerformative() == ACLMessage.REQUEST) {
					JSONArray result = list2JSON((ArrayList<Person>)obj);
					byte[] bytes = result.toString().getBytes("utf-8");
					for(int i=0;i<bytes.length; i++) {
						file.write(bytes[i]);
					}
				}
				else {
					file.writeUTF((String)obj);
				}
				file.close();
			} catch (UnreadableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
				Iterator<Sentence> it2 = person.getSentences().iterator();
				while(it2.hasNext()) {
					sentences = new HashMap<>();
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
		sd.setName("Interface");
		sd.setType("interface");
		dfd.addServices(sd);
		try {
			DFService.register( this, dfd);
			System.out.println("Agent3: Servicio publicado");
			addBehaviour(new Interfaz());
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
}
