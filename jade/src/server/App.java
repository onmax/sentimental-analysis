package server;

import jade.Boot;
// jade.Boot -gui agent1:agents.Agent1("./messages.txt")
public class App {
	public static void main(String [] args) {
		System.out.println("TEst " + args[0]);
		//Boot.printUsage();
		Boot.main(args);
	}
}
