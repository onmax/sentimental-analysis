package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveMessageBehaviour extends OneShotBehaviour { 
	private static final MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
			MessageTemplate.MatchPerformative(ACLMessage.FAILURE));
	private Agent agent;
	private Object obj;
	private String tipo;
	private AID receiver;
	public ReceiveMessageBehaviour(Agent agent,Object msg, String tipo, AID receiver) {
		super(agent);
		this.obj = msg;
		this.tipo = tipo;
		this.receiver = receiver;
	}
	public void action() {
		ACLMessage message = this.myAgent.blockingReceive(mt);
		
	}
	public Object getMessage() {
		return this.obj;
	}

}