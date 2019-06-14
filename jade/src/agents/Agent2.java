package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class Agent2 extends Agent {
	private ArrayList<String> userData;
	private AID addressAgent3;
	class getAddress extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		public void action() {
			DFAgentDescription template=new DFAgentDescription();
			ServiceDescription templateSd=new ServiceDescription();
			templateSd.setName("Interfaz");
			templateSd.setType("Interfaz");
			template.addServices(templateSd);
			//			SearchConstraints sc = new SearchConstraints();
			//			sc.setMaxResults(new Long(1));
			try {
				boolean encontrado = false;
				while(!encontrado) {
					DFAgentDescription [] results = DFService.search(this.myAgent, template);
					if(results.length > 0) {
						System.out.println("Agent2: Encontrado el servicio");
						DFAgentDescription dfd = results[0];
						System.out.println("Nombre del agente: " + dfd.getName().getName());
						addressAgent3 = dfd.getName();
						encontrado = true;
					}
				}
			}catch (FIPAException e) {
				e.printStackTrace();
			}
		}
	}
	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("EnviarMensajes");
		sd.setType("Mensajes");
		dfd.addServices(sd);
		try {
			DFService.register( this, dfd);
			System.out.println("Agent2: Servicio publicado");
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReceiveMessageBehaviour rcv = new ReceiveMessageBehaviour(this);
		addBehaviour(rcv);
		addBehaviour(new getAddress());
		addBehaviour(new SendMessageBehaviour(this, rcv.getMessage(), "REQUEST", addressAgent3));
	}

}
