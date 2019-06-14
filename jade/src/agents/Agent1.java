package agents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;;

public class Agent1 extends Agent {

	private static final long serialVersionUID = 1L;
	private Object[] parameters;
	private HashMap<String,String> messages = new HashMap<String,String>();
	private AID addressAgent2;
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
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				AID r = new AID();
				r.setName("AgenteReceiver@10.151.209.127:1099/JADE");
				r.addAddresses("http://10.151.209.127:7778/acc");
				msg.addReceiver(r);

				file.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("No existe el fichero");
			} catch (IOException e) {
				System.out.println("No se ha podido leer el fichero");
			}
		}
	}
	class getAddress extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		public void action() {
			DFAgentDescription template=new DFAgentDescription();
			ServiceDescription templateSd=new ServiceDescription();
			templateSd.setName("EnviarMensajes");
			templateSd.setType("Mensajes");
			template.addServices(templateSd);
			//			SearchConstraints sc = new SearchConstraints();
			//			sc.setMaxResults(new Long(1));
			try {
				boolean encontrado = false;
				while(!encontrado) {
					DFAgentDescription [] results = DFService.search(this.myAgent, template);
					if(results.length > 0) {
						System.out.println("Agent1: Encontr� el servicio");
						DFAgentDescription dfd = results[0];
						System.out.println("Nombre del agente: " + dfd.getName().getName());
						addressAgent2 = dfd.getName();
						encontrado = true;
					}
				}
			}catch (FIPAException e) {
				e.printStackTrace();
			}
		}
	}

	public void setup(){
		addBehaviour(new getAddress());
		parameters = getArguments();
		if(parameters == null){
			System.out.println("Introduce el argumento");
		}else{
			
			DataProccessing dp = new DataProccessing();
			addBehaviour(dp);
		}
		addBehaviour(new SendMessageBehaviour(this, messages, "REQUEST", addressAgent2));
		
	}
}
