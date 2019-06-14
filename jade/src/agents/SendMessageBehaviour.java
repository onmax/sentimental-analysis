package agents;

import java.io.IOException;
import java.io.Serializable;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendMessageBehaviour extends OneShotBehaviour {
	private String tipo;
	private Object obj;
	private AID receiver; 
	public SendMessageBehaviour(Agent agent,Object msg, String tipo, AID receiver) {
		super(agent);
		this.obj = msg;
		this.tipo = tipo;
		this.receiver = receiver;
	}

	public void action() {
		ACLMessage msg = null;
		if (tipo.equals("REQUEST"))
			msg = new ACLMessage(ACLMessage.REQUEST);
		else {
			msg = new ACLMessage(ACLMessage.FAILURE);
		}
		msg.addReceiver(receiver);
		try {
			msg.setContentObject((Serializable)obj);
			this.myAgent.send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}