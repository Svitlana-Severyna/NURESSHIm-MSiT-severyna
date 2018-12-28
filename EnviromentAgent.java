package Wumpus;

import aima.core.environment.wumpusworld.HybridWumpusAgent;
import aima.core.environment.wumpusworld.WumpusAction;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusEnvironment;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EnviromentAgent extends Agent {

    private WumpusEnvironment environment;

    private Spel spel = new Spel();

    public void setup()
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Enviroment");
        sd.setName("MyEnviroment");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("I'm ready!!!!");
        WumpusCave cave = new WumpusCave(4,4,"000000PPWWGGPP0000000000SS00PP00");

        environment = new WumpusEnvironment(cave);
        spel.setAlive(true);

        environment.addAgent(spel);
        System.out.println(environment.getCave().toString());
        addBehaviour(new OfferRequestsServer());

    }
    private class OfferRequestsServer extends CyclicBehaviour{
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                String title = msg.getContent();
                System.out.println("Env" + title);
                ACLMessage reply = msg.createReply();
                if (title != null) {
                    environment.executeAction(spel, WumpusAction.valueOf(title));
                    System.out.println(environment.getAgentPosition(spel).toString());
                    System.out.println(environment.getCave().toString());
                }
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(environment.getPerceptSeenBy(spel).toString());
                myAgent.send(reply);
                myAgent.doWait(2000);
            }
            else {
                block();
            }
        }
    }

    protected void takeDown() {
        // Printout a dismissal message
        System.out.println("Buyer-agent terminating.");
    }
    public boolean done() {
        return true;
    }
}
