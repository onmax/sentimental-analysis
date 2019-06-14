package agents;

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
	private Object obj;
	public class ReceiveMessageBehaviour extends OneShotBehaviour { 
		private static final long serialVersionUID = 1L;
		private final MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchPerformative(ACLMessage.FAILURE));
		public ReceiveMessageBehaviour(Agent agent) {
			super(agent);
		}
		public void action() {
			ACLMessage message = this.myAgent.blockingReceive(mt);
			try {
				obj = message.getContentObject();
				System.out.println(this.myAgent.getName() + ": Ha llegado el mensaje");
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
		try {
			DFService.register( this, dfd);
			System.out.println("Agent3: Servicio publicado");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		addBehaviour(new ReceiveMessageBehaviour(this));

	}
}
