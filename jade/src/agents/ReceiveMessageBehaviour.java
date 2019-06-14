package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveMessageBehaviour extends OneShotBehaviour { 
	private static final MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
			MessageTemplate.MatchPerformative(ACLMessage.FAILURE));
//	private Agent agent;
	private Object obj;
	public ReceiveMessageBehaviour(Agent agent) {
		super(agent);
	}
	public void action() {
		ACLMessage message = this.myAgent.blockingReceive(mt);
		try {
			this.obj = message.getContentObject();
			System.out.println(this.myAgent.getName() + ": Ha llegado el mensaje");
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Object getMessage() {
		return this.obj;
	}

}
