package agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class Agent3 extends Agent {
	public class Nada extends OneShotBehaviour{
		public void action() {
			block();
		}
	}
	public void setup() {
		addBehaviour(new Nada());
	}
}
