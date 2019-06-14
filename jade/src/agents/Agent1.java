package agents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;;

public class Agent1 extends Agent {

	private static final long serialVersionUID = 1L;
	private Object[] parameters;
	private HashMap<String,String> messages = new HashMap<String,String>();
	
	class DataProccessing extends OneShotBehaviour{

		private static final long serialVersionUID = 1L;

		public void action() {
			try {
				RandomAccessFile file = new RandomAccessFile((String)parameters[0], "r");
				String line;
				while((line = file.readLine()) != null){
					String division [] = line.split(":", 2);
					messages.put(division[0], division[1]);
				}
				file.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("No existe el fichero");
			} catch (IOException e) {
				System.out.println("No se ha podido leer el fichero");
			}
		}
	}
	
	public void setup(){
		parameters = getArguments();
		if(parameters == null){
			System.out.println("Introduce el argumento");
		}else{
			
			DataProccessing dp = new DataProccessing();
			addBehaviour(dp);
		}
	}
}
