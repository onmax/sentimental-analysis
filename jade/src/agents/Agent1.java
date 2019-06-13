package agents;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;

public class Agent1 extends Agent {
	private static final long serialVersionUID = 1L;
	protected SimpleBehaviour simpleBehaviour;
	private HashMap <String,String> mensajes = new HashMap <String,String>();

	public void setup(){
		simpleBehaviour = new SimpleBehaviour(this){
			private static final long serialVersionUID = 1L;
			public void action(){
				FileReader file;
				BufferedReader br = null;
				try {
					file = new FileReader("./messages.txt");
					br = new BufferedReader (file);
					String line;

					while ((line = br.readLine()) != null){
						String [] parts = line.split(":",2);
						mensajes.put(parts[0], parts[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public boolean done() {
				return true;
			}

		};
		addBehaviour(simpleBehaviour);
	}
}
