package agents;

import java.util.HashMap;
import java.util.Map;

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
	public class ResponderBehaviour extends OneShotBehaviour
	{
		private final MessageTemplate mt1 =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		//						,MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		public ResponderBehaviour(Agent agent)
		{
			super(agent);
		}
		public void action()
		{
			ACLMessage aclMessage = myAgent.blockingReceive(mt1);
			System.out.println("Receiver: Mensaje recibido");
			System.out.println("Receiver: " + aclMessage.getConversationId());
			try {
				HashMap<String, String> res = (HashMap<String,String>)aclMessage.getContentObject();
				for(Map.Entry<String, String> value: res.entrySet()) {
					System.out.println("Clave: " + value.getKey() + " Valor: " + value.getValue());
				}
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
//		addBehaviour(new defineService(this));
	}

}
