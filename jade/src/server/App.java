package server;

import jade.Boot;

public class App {
	public static void main(String [] args) {
		String [] jadeArgs = {"-gui", "-host", "localhost", "-port", "12344", "agent1:agents.Agent1(\"./messages.txt\")"};
		Boot.main(jadeArgs);
	}
}
