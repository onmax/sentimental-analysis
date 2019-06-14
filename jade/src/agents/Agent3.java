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

	public class Interfaz extends OneShotBehaviour { 
		private static final long serialVersionUID = 1L;
		private final MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchPerformative(ACLMessage.FAILURE));

		public void action() {
			ACLMessage message = this.myAgent.blockingReceive(mt);
			try {
				//Comprobar el tipo de mensaje failure o request: No se como hacerlo

				Object obj = message.getContentObject();
				System.out.println("Agente3: Ha llegado el mensaje");
				System.out.println("Mensaje: " + obj);
				
				//INSERTAR CODIGO AGENTE 3: No se donde está =)
				
			} catch (UnreadableException e) {
				e.printStackTrace();
				//Enviar error a la api
			}

		}
	}

	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		//Cambiar nombre
		sd.setName("Interfaz");
		//No se para que ponemos type
		sd.setType("Interfaz");
		dfd.addServices(sd);
		sd.setName("Error");
		sd.setType("Error");
		try {
			DFService.register( this, dfd);
			System.out.println("Agent3: Servicio publicado");
		} catch (FIPAException e) {
			//Enviar error a la api
			e.printStackTrace();
		}
		addBehaviour(new Interfaz());

	}
}
