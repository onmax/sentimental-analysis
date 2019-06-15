package agents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.HashMap;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
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
					String division [] = new String(line.getBytes("ISO-8859-1"), "UTF-8").split(":", 2);
					messages.put(division[0], division[1]);
				}
				file.close();
				AID receiver = getAddress("EnviarMensajes");
				send(receiver, "REQUEST", messages);
			} //Enviar failure: Hay que limpiarlo
			catch (FileNotFoundException e) {
				System.out.println("Agente1: No existe el fichero");
				AID receiver = getAddress("Error");
				//En el failure podemos enviar un texto explicando el error
				send(receiver, "FAILURE",null);
			} catch (IOException e) {
				System.out.println("Agente1: No se ha podido leer el fichero");
				AID receiver = getAddress("Error");
				send(receiver, "FAILURE", null);
			}
		}
	}
	public void send(AID receiver, String tipo, Object content) {
		ACLMessage request = (tipo.equals("REQUEST")) ? new ACLMessage(ACLMessage.REQUEST): new ACLMessage(ACLMessage.FAILURE);
		request.addReceiver(receiver);
//		request.setContent("Cambiar este texto por el objeto a enviar");
		if(content != null) {
			try {
				request.setLanguage(new SLCodec().getName());
				request.setEnvelope(new Envelope());
				request.getEnvelope().setPayloadEncoding("ISO8859_1");
				request.setContentObject((Serializable)content);
			} catch (IOException e) {
				
			}
		}
		this.send(request);
		System.out.println("Agente1: Mensaje enviado");
	}
	public AID getAddress (String service){
		AID res = new AID();
		DFAgentDescription template=new DFAgentDescription();
		ServiceDescription templateSd=new ServiceDescription();
		templateSd.setName(service);
//		templateSd.setName("EnviarMensajes");
//		templateSd.setType("Mensajes");
		template.addServices(templateSd);
		try {
			boolean encontrado = false;
			System.out.println("Agent1: Buscar servicio");
			while(!encontrado) {
				DFAgentDescription [] results = DFService.search(this, template);
				if(results.length > 0) {
					System.out.println("Agent1: Encontrado el servicio");
					DFAgentDescription dfd = results[0];
					System.out.println("Nombre del agente: " + dfd.getName().getName());
					System.out.println("Direccion: " + dfd.getName().getAddressesArray()[0]);
					encontrado = true;
					res = dfd.getName();
				}
			}
		}
		catch (FIPAException e) {
			e.printStackTrace();
		}
		return res;
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
